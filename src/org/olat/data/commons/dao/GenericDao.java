/**
 * OLAT - Online Learning and Training<br>
 * http://www.olat.org
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); <br>
 * you may not use this file except in compliance with the License.<br>
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,<br>
 * software distributed under the License is distributed on an "AS IS" BASIS, <br>
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. <br>
 * See the License for the specific language governing permissions and <br>
 * limitations under the License.
 * <p>
 * Copyright (c) since 2004 at Multimedia- & E-Learning Services (MELS),<br>
 * University of Zurich, Switzerland.
 * <p>
 */
package org.olat.data.commons.dao;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;

public interface GenericDao<T> {

    /**
     * Returns null if entity not found.
     */
    T findById(Long id);

    T save(T entity);

    void save(Collection<T> entities);

    T update(T entity);

    void saveOrUpdate(T entity);

    void saveOrUpdate(Collection<T> entities);

    void delete(T entity);

    T create();

    List<T> findAll();

    Criteria createCriteria();

    List<T> findByCriteria(Map<String, Object> restrictionNameValues);

    Query getNamedQuery(String name);

    void setType(Class<T> type);

    List<T> getNamedQueryListResult(String queryName, Map<String, Object> queryParameters);

    Iterator<T> getNamedQueryIteratorResult(String queryName, Map<String, Object> queryParameters);

    List<Long> getNamedQueryEntityIds(String queryName, Map<String, Object> queryParameters);

}
