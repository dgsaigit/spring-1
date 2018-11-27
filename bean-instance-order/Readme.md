**Spring实例化Bean的顺序**

# 1 背景

   在使用Spring时，Bean之间会有些依赖，比如一个Bean A实例化时需要用到Bean B,那么B应该在A之前实例化好。很多时候Spring智能地为我们做好了这些工作，但某些情况下可能不是，比如Springboot的@AutoConfigureAfter注解，手动的指定Bean的实例化顺序。了解Spring内Bean的解析，加载和实例化顺序机制有助于我们更好的使用Spring/Springboot，避免手动的去干预Bean的加载过程，搭建更优雅的框架。

# 2 初始化过程

   ApplicationContext内置一个BeanFactory对象，作为实际的Bean工厂，和Bean相关业务都交给BeanFactory去处理。在BeanFactory实例化所有非延迟加载的单例Bean时，遍历beanDefinitionNames 集合，按顺序实例化指定名称的Bean。beanDefinitionNames 属性是Spring在加载Bean Class生成的BeanDefinition时，为这些Bean预先定义好的名称。

   BeanFactory在加载一个BeanDefinition（也就是加载Bean Class）时，将相应的beanName存入beanDefinitionNames属性中，在加载完所有的BeanDefinition后，执行Bean实例化工作，此时会依据beanDefinitionNames的顺序来有序实例化Bean，也就是说Spring容器内Bean的加载和实例化是有顺序的，而且近似一致，当然仅是近似。

  Spring在初始化容器时，会先解析和加载所有的Bean Class，如果符合要求则通过Class生成BeanDefinition，存入BeanFactory中，在加载完所有Bean Class后，开始有序的通过BeanDefinition实例化Bean。

# 3 Bean实例化顺序总结

   配置类可以是Spring容器的起始配置类，也可以是通过@ComponentScan扫描得到的类，也可以是通过@Import引入的类。如果这个类上含有@Configuration，@Component，@ComponentScan，@Import，@ImportResource注解中的一个，或者内部含有@Bean标识的方法，那么这个类就是一个配置类，Spring就会按照一定流程去解析这个类上的信息。

如果有一个启动配置类A:

如果A是通过@ComponentScan扫描到的

1) 那么当前类A,立即被加载、实例化

2) 如果类A上有注解@ComponentScan，通过扫描的配置类立即被加载、实例化(同一包下的配置类顺序加载)

