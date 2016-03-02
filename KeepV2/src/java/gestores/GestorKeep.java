/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestores;

import hibernate.Keep;
import hibernate.Usuario;
import java.math.BigInteger;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 *
 * @author Gonzalo
 */
public class GestorKeep {
    
    public static JSONObject registrarNota(Keep nota, String usuario){
        Configuration configuration = new Configuration().configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
                applySettings(configuration.getProperties());
        SessionFactory factory = configuration.buildSessionFactory(builder.build());
        Session sesion = factory.openSession();
        sesion.beginTransaction();
        Usuario u = (Usuario)sesion.get(Usuario.class, usuario);
        nota.setUsuario(u);
        sesion.save(nota);
        Long id = ((BigInteger) sesion.createSQLQuery
            ("select last_insert_id()").uniqueResult())
            .longValue();       
        sesion.getTransaction().commit();
        sesion.flush();
        sesion.close();
        JSONObject obj = new JSONObject();
        obj.put("r", id);
        return obj;
    }
    
    public static JSONObject obtenerNotas(String usuario){
        Configuration configuration = new Configuration().configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
                applySettings(configuration.getProperties());
        SessionFactory factory = configuration.buildSessionFactory(builder.build());
        Session sesion = factory.openSession();
        sesion.beginTransaction();
        
        String hql = "from Keep where login = :login";
        Query q = sesion.createQuery(hql);
        q.setString("login", usuario);
        List<Keep> notas = q.list();
        
        sesion.getTransaction().commit();
        sesion.flush();
        sesion.close();
        JSONArray array= new JSONArray();
        for(Keep nota: notas){
            JSONObject obj = new JSONObject();
            obj.put("idAndroid", nota.getIdAndroid());
            obj.put("contenido", nota.getContenido());
            obj.put("estado", nota.getEstado());
            array.put(obj);
        }
        JSONObject obj2 = new JSONObject();
        obj2.put("r", array);
        return obj2;
    }
    
    public static void registrarNota(Keep nota){
        Configuration configuration = new Configuration().configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
                applySettings(configuration.getProperties());
        SessionFactory factory = configuration.buildSessionFactory(builder.build());
        Session sesion = factory.openSession();
        sesion.beginTransaction();
        sesion.save(nota);
        sesion.getTransaction().commit();
        sesion.flush();
        sesion.close();
    }
    
    public static JSONObject eliminarNota(Keep nota, String usuario){
        Configuration configuration = new Configuration().configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
                applySettings(configuration.getProperties());
        SessionFactory factory = configuration.buildSessionFactory(builder.build());
        Session sesion = factory.openSession();
        sesion.beginTransaction();
        Usuario usu = (Usuario)sesion.get(Usuario.class, usuario);
        nota.setUsuario(usu);
        String hql = "delete from Keep where login = :login and idAndroid= :idan";
        Query q = sesion.createQuery(hql);
        q.setString("login", usuario);
        q.setInteger("idan", nota.getIdAndroid());
        q.executeUpdate();

        sesion.getTransaction().commit();
        sesion.flush();
        sesion.close();
        return new JSONObject();
    }
    
    public static List<Keep> listarNotas(String usuario){
       Configuration configuration = new Configuration().configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
                applySettings(configuration.getProperties());
        SessionFactory factory = configuration.buildSessionFactory(builder.build());
        Session sesion = factory.openSession();
        sesion.beginTransaction();
        
        String hql = "from Keep where login = :login";
        Query q = sesion.createQuery(hql);
        q.setString("login", usuario);
        List<Keep> notas = q.list();
        
        sesion.getTransaction().commit();
        sesion.flush();
        sesion.close();
        
        return notas;
    }
    
    public static JSONObject eliminarNotaWeb(int id){
        Configuration configuration = new Configuration().configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
                applySettings(configuration.getProperties());
        SessionFactory factory = configuration.buildSessionFactory(builder.build());
        Session sesion = factory.openSession();
        sesion.beginTransaction();
        String hql = "delete from Keep where id= :idan";
        Query q = sesion.createQuery(hql);
        q.setInteger("idan", id);
        q.executeUpdate();
        sesion.getTransaction().commit();
        sesion.flush();
        sesion.close();
        return new JSONObject();
    }
    
    public static void actualizarNota(int id, String cont){
         Configuration configuration = new Configuration().configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
                applySettings(configuration.getProperties());
        SessionFactory factory = configuration.buildSessionFactory(builder.build());
        Session sesion = factory.openSession();
        sesion.beginTransaction();
        Keep nota= (Keep) sesion.get(Keep.class, id);
        nota.setContenido(cont);
        sesion.getTransaction().commit();
        sesion.flush();
        sesion.close();
    }
}
