package com.sidrerias.util;


import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtils {
    // Una variable estática que guarda la única instancia de SessionFactory.
public static final SessionFactory sessionFactory = buildSessionFactory();
// Método privado que construye la SessionFactory.
public static SessionFactory buildSessionFactory() {
    try {
        StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(standardRegistry).getMetadataBuilder().build();
        return metadata.getSessionFactoryBuilder().build();
    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("Error al crear la sesión de Hibernate.");
    } }    // Método público estático que devuelve la única instancia de SessionFactory.
public static SessionFactory getSessionFactory() {
    return sessionFactory;    }
// Método público estático para cerrar la SessionFactory.
public static void shutdown() {
    // Cierra los caches y las conexiones de la pool.
    getSessionFactory().close();
}}
