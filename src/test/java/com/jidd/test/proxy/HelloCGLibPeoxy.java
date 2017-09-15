package com.jidd.test.proxy;

import java.lang.reflect.Method;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

public class HelloCGLibPeoxy implements MethodInterceptor {

	private Object targetObject;

	public Object newProxy(Object targetObject) {
		this.targetObject = targetObject;
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(this.targetObject.getClass());
		enhancer.setCallback(this);
		// 返回代理对象
		return enhancer.create();
	}

	/** 
     * proxy        带来对象本身 
     * method       被拦截到的方法 
     * args          方法的参数 
     * methodProxy  方法的代理对象 
     */
	@Override
	public Object intercept(Object proxy, Method method, Object[] args,
			MethodProxy methodProxy) throws Throwable {
		Object ret = null;
		try {
			// 执行前置的方法
			System.out.println("do some thing before");
			// 通过反射，执行目标方法，也就是你的主要目的
			// 调用目标对象的真实方法
			ret = method.invoke(this.targetObject, args);
			// 执行后置的方法
			System.out.println("do some thing after");
			// ret接受存在的返回值，不存在返回值则为Null
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static void main(String[] args) {
		HelloCGLibPeoxy factory = new HelloCGLibPeoxy();
		// 创建代理对象，这是这个代理对象是UserManagerImpl的子类
		HelloWordImpl impl = (HelloWordImpl) factory
				.newProxy(new HelloWordImpl());
		impl.sayHello();
	}
}
