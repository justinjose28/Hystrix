/**
 * Copyright 2017 Netflix, Inc.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.hystrix.contrib.javanica.test.guice.error;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.netflix.hystrix.contrib.javanica.test.common.error.BasicObservableErrorPropagationTest;
import org.junit.BeforeClass;

import static com.netflix.hystrix.contrib.javanica.test.guice.GuiceTestUtils.getBaseInjector;

public class ObservableErrorPropagationTest extends BasicObservableErrorPropagationTest {

    private static Injector injector;

    @BeforeClass
    public static void setup() {
        injector = getBaseInjector().createChildInjector(new AbstractModule() {

            @Override
            protected void configure() {
                bind(UserService.class);

            }
        });
    }

    @Override
    protected UserService createUserService() {
        return injector.getInstance(UserService.class);
    }

}
