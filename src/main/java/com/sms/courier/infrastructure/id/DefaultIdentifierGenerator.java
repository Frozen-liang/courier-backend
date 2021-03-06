/*
 * Copyright (c) 2011-2021, baomidou (jobob@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sms.courier.infrastructure.id;

/**
 * 默认生成器.
 *
 * @author sd-wangtaicheng@sdcncsi.com.cn nieqiuqiu
 * @since 2019-10-15
 * @since 3.3.0
 */
public class DefaultIdentifierGenerator implements IdentifierGenerator {

    private final Sequence sequence;

    private DefaultIdentifierGenerator() {
        this.sequence = new Sequence();
    }

    @Override
    public Long nextId(Object entity) {
        return sequence.nextId();
    }

    @Override
    public Long nextId() {
        return nextId(null);
    }

    public static DefaultIdentifierGenerator getSharedInstance() {
        return IdentifierGeneratorHolder.INSTANCE;
    }

    private static class IdentifierGeneratorHolder {


        private static final DefaultIdentifierGenerator INSTANCE = new DefaultIdentifierGenerator();
    }
}
