**Spring实例化Bean的顺序**

# 1 背景

   在使用Spring时，Bean之间会有些依赖，比如一个Bean A实例化时需要用到Bean B,那么B应该在A之前实例化好。很多时候Spring智能地为我们做好了这些工作，但某些情况下可能不是，比如Springboot的@AutoConfigureAfter注解，手动的指定Bean的实例化顺序。了解Spring内Bean的解析，加载和实例化顺序机制有助于我们更好的使用Spring/Springboot，避免手动的去干预Bean的加载过程，搭建更优雅的框架。

# 2 初始化过程

   ApplicationContext内置一个BeanFactory对象，作为实际的Bean工厂，和Bean相关业务都交给BeanFactory去处理。在BeanFactory实例化所有非延迟加载的单例Bean时，遍历beanDefinitionNames 集合，按顺序实例化指定名称的Bean。beanDefinitionNames 属性是Spring在加载Bean Class生成的BeanDefinition时，为这些Bean预先定义好的名称。

   BeanFactory在加载一个BeanDefinition（也就是加载Bean Class）时，将相应的beanName存入beanDefinitionNames属性中，在加载完所有的BeanDefinition后，执行Bean实例化工作，此时会依据beanDefinitionNames的顺序来有序实例化Bean，也就是说Spring容器内Bean的加载和实例化是有顺序的，而且近似一致，当然仅是近似。

  Spring在初始化容器时，会先解析和加载所有的Bean Class，如果符合要求则通过Class生成BeanDefinition，存入BeanFactory中，在加载完所有Bean Class后，开始有序的通过BeanDefinition实例化Bean。

# 3 Bean实例化顺序总结

   配置类可以是Spring容器的起始配置类，也可以是通过@ComponentScan扫描得到的类，也可以是通过@Import引入的类。如果这个类上含有@Configuration，@Component，@ComponentScan，@Import，@ImportResource注解中的一个，或者内部含有@Bean标识的方法，那么这个类就是一个配置类，Spring就会按照一定流程去解析这个类上的信息。

1)）解析配置类上是否有@ComponentScan注解，如果有则执行扫描动作，通过扫描得到的Bean Class会被立即解析成BeanDefinition，添加进beanDefinitionNames属性中，立即进行实例化，之后查看扫描到的Bean Class是否是一个配置类（大部分情况是，因为标识@Component注解），如果是则递归解析这个Bean Class。

2）解析@Import引入的类，如果这个类是一个配置类，则递归解析。

3）解析@Bean标识的方法，此种形式定义的Bean Class不会被递归解析



**总结**

* **通过@ComponentScan**扫描直接得到的Bean Class会被**立即加载，实例化**。

* **通过@Bean和@Import形式定义的Bean Class不会立即加载，他们会被放入一个ConfigurationClass类中，然后按照解析的顺序有序排列**。一个ConfigurationClass代表一个配置类，这个类可能是被@ComponentScan扫描到的，则此类已经被加载过了；也可能是被@Import引入的，则此类还未被加载；此类中可能含有@Bean标识的方法。

* **Spring在解析完了所有Bean Class后，开始加载ConfigurationClass**。如果这个**ConfigurationClass是被Import的**，也就是说在加载@ComponentScan时其**未被加载**，那么此时**加载ConfigurationClass代表的Bean Class**。**然后加载ConfigurationClass内的@Bean方法**。

* 在1，2中都有递归操作，也就是在解析一个Bean Class A时，发现其上能够获取到其他Bean Class B信息，此时会递归的解析Bean Class B，在解析完Bean Class B后再接着解析Bean Class A，可能在解析B时能够获取到C，那么也会先解析C再解析B，就这样**不断的递归解析**。

由上述四条可以得出@ComponentScan扫描的配置类先被实例化，然后再有@import及@Bean引入的顺序有序排列的类进行实例化。

**顺序总结：@ComponentScan > @Import > @Bean** 

**注意：**当一个Bean A内部通过**@Autowired或者@Resource**注入Bean B，那么**在实例化A时会触发B的提前实例化**，此时会注册A>B的dependsOn依赖关系，实质和@DependsOn一样，这个是Spring自动为我们处理好的。

# 4 实例说明

## 4.1 实例一

### 4.1.1 序列图

mermaid
graph LR
S[开始] -. "@"ComponentScan .->A1[Test]
A1 -. "@"Import .->A[A]
A1 -. "@"ComponentScan .->B[B]
A1 -. "@"Import .->C[C]
A1 -. "@"Bean .->H[H]
A -. "@"Import .->E[E]
E -. "@"ComponentScan .->J[J]
A -. "@"Bean .->I[I]
B -. "@"ComponentScan .->D[D]
B -. "@"ComponentScan .->F[F]
C -. "@"ComponentScan .->G[G]
C -. "@"Bean .->M[M]
C -. "@"Import .->N[N]
G -. "@"Import .->K[K]
G -. "@"Bean .->L[L]
N -. "@"Autowired .->H
D -. "@"Bean .->O[O]
F -. "@"Import .->Q[Q]
F -. "@"ImportResource .->xml[import-resource.xml]
xml -. bean .-> P[P]


Bean Class的结构图如上所示,源码参考[spring-beans-instance-order](spring-beans-instance-order),JUnit Test运行XmlTest,得出实例化顺序是：Test->B->D->F->J->G->O->Q->P->E->A->I->K->L->N->H->C->M

### 4.1.2 过程解析

未实例化顺序:即通过@Bean和@Import形式定义的Bean Class

| 序号 |                             说明                             | 实例化顺序                 | 未实例化顺序      |
| :--: | :----------------------------------------------------------: | -------------------------- | ----------------- |
|  1   |      首先注解@ComponentScan扫描到Test配置类，立即实例化      | Test                       |                   |
|  2   | 解析Test,发现注解@ComponentScan、@Import、@Bean,根据优先级，先执行@ComponentScan扫描，发现B配置类，立即实例化B | Test<br>B                  |                   |
|  3   | 解析B,发现注解@ComponentScan,执行扫描，发现D、F配置类，根据顺序立即实例化D、F | Test<br>B<br>D<br>F        |                   |
|  4   | 根据顺序，先递归解析D,发现@Bean定义的类O,此种形式定义的Bean Class不会被递归解析,而D已经被加载实例化，所以将O加入排序 | Test<br/>B
D
F               | O                 |
|  5   | 然后再解析F,发现注解@Import、@ImportResource,@Import优先级高于@ImportResource,先解析Q,Q上没注解，将Q加入排序 | Test<br/>B
D
F               | O<br/>Q           |
|  6   |     之后解析xml文件，里面有个类P的Bean创建，将P加入排序      | Test<br/>B
D
F               | O<br/>Q
P          |
|  7   | Test上@ComponentScan递归解析结束，开始解析@Import，引入A、C，根据顺序先解析A | Test<br/>B
D
F               | O<br/>Q
P          |
|  8   | 递归解析A,扫描到配置类J,加载并实例化，逐步@Import E、@Import A、@Bean | Test<br/>B
D
F
J              | O<br/>Q
P
E
A
I       |
|  9   | 递归解析C,先执行@ComponentScan扫描，扫描到配置类G,立即加载实例化，再递归解析G,逐步@Import K、@Bean L | Test<br/>B
D
F
J
G             | O<br/>Q
P
E
A
I
K
L     |
|  10  | 再解析C上注解@Import,在解析类N时，发现N依赖H,即@Autowired,实例化N时会触发H的提前实例化,将N、H加入排序 | Test<br/>B
D
F
J
G             | O<br/>Q
P
E
A
I
K
L
N
H   |
|  11  | 之后解析C上的@Bean方法，因为C是被@Import引用的，C先一步M被加入加载实例序列 | Test<br/>B
D
F
J
G             | O<br/>Q
P
E
A
I
K
L
N
H
C
M |
|  12  |         结束，开始加载实例化序列中ConfigurationClass         | Test<br/>B
D
F
J
G
O
Q
P
E
A
I
K
L
N
H
C
M |                   |
## 4.2 实例二

如果将上面实例为基础，配置类Test是通过@Import形式引入的话，Test不会立即加载实例化，要等递归结束，但N依赖H,H又是在Test下@Bean方法得到的，Test排在H前面，排在N后面，顺序又变成

B->D->F->J->G->O->Q->P->E->A->I->K->L->N->Test->H->C->M

可见代码XmlTest类中@ContextConfiguration(classes = Test.class)中Test换成Test1,JUnit Test运行XmlTest测试

# 5 文章参考

[Spring解析，加载及实例化Bean的顺序（零配置）](https://blog.csdn.net/qq_27529917/article/details/79329809)

