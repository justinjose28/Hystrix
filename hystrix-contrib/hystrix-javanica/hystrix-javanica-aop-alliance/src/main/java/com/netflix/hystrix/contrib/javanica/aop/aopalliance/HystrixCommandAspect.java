/**
 * Copyright 2017 Netflix, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.hystrix.contrib.javanica.aop.aopalliance;

import com.netflix.hystrix.contrib.javanica.aop.AbstractMetaHolderFactory;
import com.netflix.hystrix.contrib.javanica.aop.AbstractHystrixCommandAspect;
import com.netflix.hystrix.contrib.javanica.aop.AbstractHystrixCommandBuilderFactory;
import com.netflix.hystrix.contrib.javanica.aop.aopalliance.AopAllianceMetaHolder.Builder;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

import static com.netflix.hystrix.contrib.javanica.aop.aopalliance.FactoryProvider.Type.DEFAULT;

/**
 *
 * @author justinjose28
 *
 */
public class HystrixCommandAspect extends AbstractHystrixCommandAspect<AopAllianceMetaHolder, AopAllianceMetaHolder.Builder> implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        return execute(new AopAllianceMetaHolderBuilder(invocation, getActualMethod(invocation), getActualClass(invocation)).build());
    }

    @Override
    protected AbstractHystrixCommandBuilderFactory<AopAllianceMetaHolder, Builder> getCommandBuilderFactory() {
        return FactoryProvider.getCommandBuilderFactory(DEFAULT);
    }

    protected Method getActualMethod(MethodInvocation invocation) {
        Class<?> current = invocation.getThis().getClass().getSuperclass();
        Method method = null;
        while (current != Object.class) {
            try {
                method = current.getDeclaredMethod(invocation.getMethod().getName(), invocation.getMethod().getParameterTypes());
                break;
            } catch (NoSuchMethodException ex) {
                current = current.getSuperclass();
            }
        }
        return method;
    }

    protected Class<?> getActualClass(MethodInvocation invocation) {
        return invocation.getThis().getClass().getSuperclass();
    }

    private static class AopAllianceMetaHolderBuilder extends AbstractMetaHolderFactory<AopAllianceMetaHolder, AopAllianceMetaHolder.Builder> {

        private AopAllianceMetaHolderBuilder(MethodInvocation methodInvocation, Method actualMethod, Class<?> actualClass) {
            super(AopAllianceMetaHolder.builder(), actualMethod, methodInvocation.getThis(), actualClass, methodInvocation.getArguments());
        }
    }
}