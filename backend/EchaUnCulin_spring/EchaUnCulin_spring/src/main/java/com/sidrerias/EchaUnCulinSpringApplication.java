package com.sidrerias;

import com.sidrerias.modelo.*;
import com.sidrerias.util.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class EchaUnCulinSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(EchaUnCulinSpringApplication.class, args);
        System.out.println("Hola en Spring");
     crearDatosPrueba();


    }


    public static void crearDatosPrueba() {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            // Crear usuarios Gerentes
            Usuario usuario1 = new Usuario();
            usuario1.setNombre("Ernesto");
            usuario1.setApellidos("Estrada");
            usuario1.setEmail("ernesto@email.com");
            usuario1.setFechaNacimiento(getFecha("12-04-1999"));
            usuario1.setUsername("ernestoea99");
            usuario1.setRutaAvatar("https://img.freepik.com/free-vector/smiling-young-man-illustration_1308-174669.jpg");
            usuario1.setPassword(new BCryptPasswordEncoder().encode("12345678")); // Cambiar a contraseña cifrada en producción
            usuario1.setRole("GERENTE");

            Usuario usuario2 = new Usuario();
            usuario2.setNombre("Maria");
            usuario2.setApellidos("Gutierrez");
            usuario2.setEmail("maria@email.com");
            usuario2.setRutaAvatar("https://static.vecteezy.com/system/resources/thumbnails/004/899/680/small/beautiful-blonde-woman-with-makeup-avatar-for-a-beauty-salon-illustration-in-the-cartoon-style-vector.jpg");
            usuario2.setFechaNacimiento(getFecha("07-11-2001"));
            usuario2.setUsername("user_maria");
            usuario2.setPassword(new BCryptPasswordEncoder().encode("12345678"));
            usuario2.setRole("GERENTE");

            // Crear usuarios Clientes
            Usuario usuario3 = new Usuario();
            usuario3.setNombre("Luis");
            usuario3.setApellidos("Garcia");
            usuario3.setEmail("luis@email.com");
            usuario3.setFechaNacimiento(getFecha("07-11-1997"));
            usuario3.setUsername("user_luis");
            usuario3.setRutaAvatar("https://png.pngtree.com/png-vector/20230831/ourmid/pngtree-man-avatar-image-for-profile-png-image_9197908.png");
            usuario3.setPassword( new BCryptPasswordEncoder().encode("12345678"));
            usuario3.setRole("CLIENTE");

            Usuario usuario4 = new Usuario();
            usuario4.setNombre("Sara");
            usuario4.setApellidos("Gomez");
            usuario4.setEmail("sara@email.com");
            usuario4.setRutaAvatar("https://t3.ftcdn.net/jpg/06/17/13/26/360_F_617132669_YptvM7fIuczaUbYYpMe3VTLimwZwzlWf.jpg");
            usuario4.setFechaNacimiento(getFecha("07-11-1997"));
            usuario4.setUsername("user_sara"); // Cambiado para evitar duplicado
            usuario4.setPassword(new BCryptPasswordEncoder().encode("12345678") );
            usuario4.setRole("CLIENTE");

            session.save(usuario1);
            session.save(usuario2);
            session.save(usuario3);
            session.save(usuario4);
            // Crear sidrerías y asignar gerentes
            Sidreria sidreria1 = new Sidreria();
            sidreria1.setValoracion(2.7f);
            sidreria1.setNombre("Llagar el Pitu");
            sidreria1.setEscanciado(true);
            sidreria1.setUbicacion("Calle Real, 45, Oviedo, Asturias");
            sidreria1.setPrecioComensal(27.50f);
            sidreria1.setRutaCarta("../recursos/menus/menu.pdf");
            sidreria1.setGerente(usuario1);
            sidreria1.getUsuariosGustan().add(usuario3); // Relación Many-to-Many

            Sidreria sidreria2 = new Sidreria();
            sidreria2.setValoracion(3.82f);
            sidreria2.setNombre("Casa Pepe");
            sidreria2.setEscanciado(true);
            sidreria2.setPrecioComensal(19.50f);
            sidreria2.setGerente(usuario2);
            sidreria2.setUbicacion("Plaza Mayor, 12, Gijón, Asturias");
            sidreria2.setRutaCarta("../recursos/menus/menu.pdf");
            sidreria2.getUsuariosGustan().add(usuario4); // Relación Many-to-Many

            // Establecer relaciones bidireccionales
            usuario1.setSidreria(sidreria1);
            usuario2.setSidreria(sidreria2);
            usuario3.getSidreriasFavoritas().add(sidreria1);
            usuario4.getSidreriasFavoritas().add(sidreria2);

            // Persistir datos
            session.save(sidreria1);
            session.save(sidreria2);


            // Crear imágenes
            List<String> rutasSidreria1 = Arrays.asList(
                    "../imagenes/cachopo.jpg", "../imagenes/fabada.jpg", "../imagenes/pote.jpg");
            for (String ruta : rutasSidreria1) {
                ImagenSidreria imagen = new ImagenSidreria();
                imagen.setRuta(ruta);
                imagen.setSidreria(sidreria1);
                session.save(imagen);
            }

            List<String> rutasSidreria2 = Arrays.asList(
                    "../imagenes/cachopo.jpg",
                    "https://ejemplo.com/imagenes/casa_de_la_sidra_2.jpg",
                    "https://ejemplo.com/imagenes/casa_de_la_sidra_3.jpg");
            for (String ruta : rutasSidreria2) {
                ImagenSidreria imagen = new ImagenSidreria();
                imagen.setRuta(ruta);
                imagen.setSidreria(sidreria2);
                session.save(imagen);
            }

            // Crear reviews
            Review review1 = new Review();
            review1.setContenido("El lugar está bien, pero los precios son un poco altos para lo que ofrecen.");
            review1.setTitulo("Buen lugar pero algo caro");
            review1.setValoracion(2.83f);
            review1.setEditado(true);
            review1.setFechaPublicacion(new Date());
            review1.setSidreria(sidreria1);
            review1.setUsuario(usuario3); // Un cliente revisa
            session.save(review1);

            Review review2 = new Review();
            review2.setTitulo("Buena sidra");
            review2.setContenido("La sidra está muy buena, aunque el ambiente podría mejorar.");
            review2.setEditado(false);
            review2.setValoracion(2.57f);
            review2.setFechaPublicacion(new SimpleDateFormat("dd-MM-yyyy").parse("25-07-2022"));
            review2.setSidreria(sidreria2);
            review2.setRespuesta("Graciaspor el comentario, intentaremos mejorar");
            review2.setUsuario(usuario4); // Otro cliente revisa
            session.save(review2);
            // Crear más reviews
            Review review3 = new Review();
            review3.setTitulo("Excelente experiencia");
            review3.setContenido("La comida y la sidra fueron excepcionales. El personal fue muy amable.");
            review3.setEditado(false);
            review3.setValoracion(4.8f);
            review3.setFechaPublicacion(new SimpleDateFormat("dd-MM-yyyy").parse("12-09-2023"));
            review3.setSidreria(sidreria1);
            review3.setUsuario(usuario3); // Un cliente revisa
            session.save(review3);

            Review review4 = new Review();
            review4.setTitulo("Regular atención al cliente");
            review4.setContenido("El lugar tiene potencial, pero la atención fue muy lenta y desorganizada.");
            review4.setEditado(true);
            review4.setValoracion(2.0f);
            review4.setFechaPublicacion(new SimpleDateFormat("dd-MM-yyyy").parse("15-03-2023"));
            review4.setSidreria(sidreria2);
            review4.setRespuesta("Lamentamos tu experiencia, trabajaremos en mejorar el servicio.");
            review4.setUsuario(usuario4); // Otro cliente revisa
            session.save(review4);

            Review review5 = new Review();
            review5.setTitulo("Perfecto para un día especial");
            review5.setContenido("La calidad de la comida fue espectacular y el ambiente muy acogedor.");
            review5.setEditado(false);
            review5.setValoracion(5.0f);
            review5.setFechaPublicacion(new SimpleDateFormat("dd-MM-yyyy").parse("01-01-2024"));
            review5.setSidreria(sidreria1);
            review5.setUsuario(usuario3); // Un cliente revisa
            session.save(review5);

            Review review6 = new Review();
            review6.setTitulo("Demasiado ruido");
            review6.setContenido("El lugar estaba lleno y era muy ruidoso. No pudimos disfrutar de la conversación.");
            review6.setEditado(true);
            review6.setValoracion(3.0f);
            review6.setFechaPublicacion(new SimpleDateFormat("dd-MM-yyyy").parse("10-05-2023"));
            review6.setSidreria(sidreria2);
            review6.setRespuesta("Gracias por tu comentario. Trabajaremos en mejorar la acústica.");
            review6.setUsuario(usuario4); // Otro cliente revisa
            session.save(review6);

            Review review7 = new Review();
            review7.setTitulo("Calidad consistente");
            review7.setContenido("Siempre venimos aquí porque la calidad nunca decepciona.");
            review7.setEditado(false);
            review7.setValoracion(4.5f);
            review7.setFechaPublicacion(new SimpleDateFormat("dd-MM-yyyy").parse("20-11-2023"));
            review7.setSidreria(sidreria1);
            review7.setUsuario(usuario4); // Otro cliente revisa
            session.save(review7);

            Review review8 = new Review();
            review8.setTitulo("Podría mejorar");
            review8.setContenido("La comida estuvo bien, pero el servicio dejó mucho que desear.");
            review8.setEditado(true);
            review8.setValoracion(3.2f);
            review8.setFechaPublicacion(new SimpleDateFormat("dd-MM-yyyy").parse("18-06-2023"));
            review8.setSidreria(sidreria2);
            review8.setRespuesta("Gracias por tu comentario, capacitaré mejor a nuestro personal.");
            review8.setUsuario(usuario3); // Un cliente revisa
            session.save(review8);

            // Commit de la transacción
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
            HibernateUtils.shutdown();
        }
    }


    public static Date getFecha(String fechaTexto) {
        String formatoFecha = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(formatoFecha);
        try {
            return sdf.parse(fechaTexto);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }



}
