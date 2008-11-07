package ${hss_service_package}.impl;

import java.io.Serializable;

import org.nestframework.commons.hibernate.HibernateManagerSupport;

import ${hss_service_package}.IRootManager;

public abstract class RootManager<T, K extends Serializable> extends HibernateManagerSupport<T, K> implements IRootManager<T, K> {

}