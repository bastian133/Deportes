/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexion;

import Metodos.Fechas;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import static java.lang.System.out;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 *
 * @author user
 *
 * Clase que permite conectar a bases de datos
 */
public class Conectar {

    Connection Conect = null;

    private static String correoF = "";

    public static InputStream img;

    public static String codigo = "";

    public static Fechas fecha = new Fechas();

    public static Random rm = new Random();

    public Connection conexion() {

        try {
            //Cargamos el Driver MySQL
            Class.forName("com.mysql.jdbc.Driver");
            Class.forName("org.gjt.mm.mysql.Driver");
            Conect = DriverManager.getConnection("jdbc:mysql://localhost/deportepoli?"
                    + "user=root&password=");
            System.out.println("Se conecto");

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Conectar.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("No se conecto");
        } catch (SQLException ex) {
            Logger.getLogger(Conectar.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("No se conecto");
        }

        return Conect;
    }

    //Validar el correo//
    public boolean checkEmail(String email) {
        if (correoInvalido(email)) {
            System.out.println("Correo no valido");
        } else {
            // Establecer el patron
            Pattern p = Pattern.compile("[-\\w\\.]+@[\\.\\w]+\\.\\w+");

            // Asociar el string al patron
            Matcher m = p.matcher(email);

            // Comprobar si encaja
            return m.matches();
        }

        return false;
    }

    public static String[] invalidos = new String[]{"example", "example@.com.com",
        "exampel101@test.a", "exampel101@.com", ".example@test.com",
        "example**()@test.com", "example@%*.com",
        "example..101@test.com", "example.@test.com",
        "test@example101.com", "example@test@test.com",
        "example@test.com.a5"};

    public static boolean correoInvalido(String correo) {
        for (int i = 0; i < invalidos.length; i++) {
            if (correo.equalsIgnoreCase(invalidos[i])) {
                return true;
            }
        }

        return false;

    }
    //------------------------------------------------//

    // Validacion correo con la base de daros//
    public boolean validar(String correo) {
        boolean esta = false;
        String clave = "";

        try {

            Connection cn = conexion();

            PreparedStatement pstm = cn.prepareStatement(" SELECT clave"
                    + " FROM usuarios "
                    + " WHERE e_mail = '" + correo + "'");
            //Se crea un objeto donde se almacena el resultado
            //Y con el comando executeQuery se ejecuta la consulta en la base de datos
            ResultSet res = pstm.executeQuery();
            //Recorre el resultado para mostrarlo en los jtf
            while (res.next()) {
                //jTF_identificacion.setText(res.getString( "id_persona" ));
                clave = (res.getString("clave"));

            }

            if (clave.equals("")) {
                esta = false;
            } else {
                esta = true;
            }
            res.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "El correo no existe");
            System.err.println(e.getMessage());
        }

        return esta;
    }

    public boolean contrasena(String correo, String contrasena) {
        boolean esta = false;

        String clave = "";

        try {

            Connection cn = conexion();

            PreparedStatement pstm = cn.prepareStatement(" SELECT clave"
                    + " FROM usuarios"
                    + " WHERE e_mail = '" + correo + "'");
            //Se crea un objeto donde se almacena el resultado
            //Y con el comando executeQuery se ejecuta la consulta en la base de datos
            ResultSet res = pstm.executeQuery();
            //Recorre el resultado para mostrarlo en los jtf
            while (res.next()) {
                //jTF_identificacion.setText(res.getString( "id_persona" ));
                clave = (res.getString("clave"));

            }

            if (contrasena.equals(clave)) {
                esta = true;
            } else {
                esta = false;
            }
            res.close();

        } catch (SQLException e) {

        }

        return esta;
    }

    //------------------------------------------------//
    //Mirar que tipo de perfil es//
    public int tipou(String correo) {
        int h = 0;
        try {

            Connection cn = conexion();

            PreparedStatement pstm = cn.prepareStatement(" SELECT tipo_usuario"
                    + " FROM usuarios "
                    + " WHERE e_mail = '" + correo + "'");
            //Se crea un objeto donde se almacena el resultado
            //Y con el comando executeQuery se ejecuta la consulta en la base de datos
            ResultSet res = pstm.executeQuery();
            //Recorre el resultado para mostrarlo en los jtf
            while (res.next()) {
                //jTF_identificacion.setText(res.getString( "id_persona" ));
                h = (res.getInt("tipo_usuario"));

            }

            res.close();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return h;
    }
    //------------------------------------------------//

    public int Ver_estudiante(String Id_estudiante) {
        int i = 0;

        try {

            Connection cn = conexion();

            PreparedStatement pstm = cn.prepareStatement(" SELECT count(*)"
                    + " FROM estudiante"
                    + " WHERE id = '" + Id_estudiante + "'");
            //Se crea un objeto donde se almacena el resultado
            //Y con el comando executeQuery se ejecuta la consulta en la base de datos
            ResultSet res = pstm.executeQuery();
            //Recorre el resultado para mostrarlo en los jtf
            while (res.next()) {
                //jTF_identificacion.setText(res.getString( "id_persona" ));
                i = (res.getInt("count(*)"));
            }
            res.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        return i;
    }

    //------------------------------------------------//
    //Pruba para insertar imagenes
    public void InsertarImagen() throws FileNotFoundException {
        File imageFile = new File("C:\\Users\\user\\Desktop\\Server3\\server-icon.png");  //Direccion de la imagen
        FileInputStream fis = new FileInputStream(imageFile);

        try {
            Connection cn = conexion();
            PreparedStatement rs = cn.prepareStatement("INSERT INTO usuario"
                    + "(e_mail,Clave,tipo_usuario,Imagen)"
                    + "VALUES (?,?,?,?)");
            rs.setString(1, "C@gmail.com");
            rs.setString(2, "C@gmail.com");
            rs.setInt(3, 0);
            rs.setBinaryStream(4, (InputStream) fis, (int) imageFile.length());//Convertir los datos a blond
            rs.executeUpdate();

        } catch (Exception e) {
        }
    }

    public OutputStream verImagen() {
        try {
            Connection cn = conexion();
            PreparedStatement rs = cn.prepareStatement("SELECT Imagen FROM usuario"
                    + " WHERE e_mail='carlos_moreno82151@elpoli.edu.co'");
            ResultSet res = rs.executeQuery();
            int i = 0;
            while (res.next()) {
                img = res.getBinaryStream(1);
                OutputStream out = new FileOutputStream(new File("C:\\Users\\user\\Desktop\\Server3\\Imagendelusuari00o.png"));
                i++;
                int c = 0;
                while ((c = img.read()) > -1) {
                    System.out.println(c);
                    out.write(c);
                }
            }
            InputStreamReader isReader = new InputStreamReader(img);
            //Creating a BufferedReader object
            BufferedReader reader = new BufferedReader(isReader);
            StringBuffer sb = new StringBuffer();
            String str;
            while ((str = reader.readLine()) != null) {
                sb.append(str);
            }
            JOptionPane.showMessageDialog(null, "La imagen esta" + sb.toString());
            out.close();
            img.close();
            return out;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            return out;

        }
    }

    public int matricular(String id, String Nombre, String Apellido, String fechan) {
        int bien = 0;
        try {

            Connection cn = conexion();
            PreparedStatement rs = cn.prepareStatement("INSERT INTO estudiante"
                    + "(id,nombre,apellido,fecha_nacimiento)"
                    + "VALUES (?,?,?,?)");

            rs.setString(1, id);
            rs.setString(2, Nombre);
            rs.setString(3, Apellido);
            rs.setString(4, fechan);

            rs.executeUpdate();
            bien = 1;
        } catch (Exception e) {
            bien = 0;
            JOptionPane.showMessageDialog(null, e);
        }
        return bien;

    }

    public int estudianteTutor(String id, String idtutor) {
        int bien = 0;
        try {

            Connection cn = conexion();
            PreparedStatement rs = cn.prepareStatement("INSERT INTO estudiante_tutor"
                    + "(estudiante_id,tutor_id)"
                    + "VALUES (?,?)");

            rs.setString(1, id);
            rs.setString(2, idtutor);

            rs.executeUpdate();
            bien = 1;
        } catch (Exception e) {
            bien = 0;
            JOptionPane.showMessageDialog(null, e);
        }
        return bien;

    }

    public int tutor(String parentensco, String id, String Direccion) {
        int bien = 0;
        try {

            Connection cn = conexion();
            PreparedStatement rs = cn.prepareStatement("INSERT INTO tutor"
                    + "(parentesco,id,direccion)"
                    + "VALUES (?,?,?)");

            rs.setString(1, parentensco);
            rs.setString(2, id);
            rs.setString(3, Direccion);

            rs.executeUpdate();
            bien = 1;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            bien = 0;
        }
        return bien;

    }

    public int persona(String id, String nombre, String apellido, String telefono, String celular) {
        int bien = 0;
        try {

            Connection cn = conexion();
            PreparedStatement rs = cn.prepareStatement("INSERT INTO persoan"
                    + "(id,nombre,apellido,telefono,celular)"
                    + "VALUES (?,?,?,?,?)");

            rs.setString(1, id);
            rs.setString(2, nombre);
            rs.setString(3, apellido);
            rs.setString(4, telefono);
            rs.setString(5, celular);

            rs.executeUpdate();
            bien = 1;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            bien = 0;
        }
        return bien;

    }

    public int matricularCurso(String fechn, String deporte, String id) {

        int categoria = fecha.calcularCategoria(fechn);

        int controlador = 0;

        //Creamos un codigo totalmente aleatorio el cual va a quedar como codigo de matricula
        int vect[] = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        for (int i = 0; i < 5; i++) {
            codigo += rm.nextInt(10);
        }
        //Creamos un codigo totalmente aleatorio el cual va a quedar como codigo de matricula

        try {

            Connection cn = conexion();
            PreparedStatement rs = cn.prepareStatement("INSERT INTO matricula"
                    + "(codigo,estudiante_id)"
                    + "VALUES (?,?)");

            rs.setString(1, codigo);
            rs.setString(2, id);

            rs.executeUpdate();

            controlador = 1;
            Matricula_Deporte(codigo, deporte);
        } catch (Exception e) {
            controlador = 0;
            JOptionPane.showMessageDialog(null, e);
        }
        return controlador;
    }

    public void Matricula_Deporte(String codigomatricula, String Deporte) {
        String codigode = verCodigoDeporte(Deporte);
        int bien = 0;
        try {
            Connection cn = conexion();
            PreparedStatement rs = cn.prepareStatement("INSERT INTO matricula_deporte"
                    + "(matricula_codigo,deporte_codigo)"
                    + "VALUES (?,?)");

            rs.setString(1, codigomatricula);
            rs.setString(2, codigode);

            rs.executeUpdate();
            bien = 1;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Metodo que devuelve el codigo segun el tipo de deporte
     *
     * @param deporte
     * @return String
     */
    public String verCodigoDeporte(String deporte) {
        String codigod = "";

        try {

            Connection cn = conexion();

            PreparedStatement pstm = cn.prepareStatement(" SELECT codigo "
                    + " FROM deporte  "
                    + " WHERE descripcion = '" + deporte + "'");
            //Se crea un objeto donde se almacena el resultado
            //Y con el comando executeQuery se ejecuta la consulta en la base de datos
            ResultSet res = pstm.executeQuery();
            //Recorre el resultado para mostrarlo en los jtf
            while (res.next()) {
                //jTF_identificacion.setText(res.getString( "id_persona" ));
                codigod = (res.getString("codigo"));
            }
            res.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return codigod;
    }

    public String[] deportes() {

        String Deportes[] = new String[cuantosDeportes()];
        try {

            Connection cn = conexion();

            PreparedStatement pstm = cn.prepareStatement(" SELECT descripcion "
                    + " FROM deporte  ");
            //Se crea un objeto donde se almacena el resultado
            //Y con el comando executeQuery se ejecuta la consulta en la base de datos
            ResultSet res = pstm.executeQuery();
            //Recorre el resultado para mostrarlo en los jtf
            int i = 0;
            while (res.next()) {
                //jTF_identificacion.setText(res.getString( "id_persona" ));
                Deportes[i] = (res.getString("descripcion"));
                i++;
            }
            res.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return Deportes;
    }

    /**
     * Metodo que permite saber cuantos deportes hay registrados
     *
     * @return int cantidad de deportes
     */
    public int cuantosDeportes() {
        int count = 0;

        try {

            Connection cn = conexion();

            PreparedStatement pstm = cn.prepareStatement(" SELECT count(*)"
                    + " FROM deporte ");
            //Se crea un objeto donde se almacena el resultado
            //Y con el comando executeQuery se ejecuta la consulta en la base de datos
            ResultSet res = pstm.executeQuery();
            //Recorre el resultado para mostrarlo en los jtf
            while (res.next()) {
                //jTF_identificacion.setText(res.getString( "id_persona" ));
                count = (res.getInt("count(*)"));

            }
//            JOptionPane.showMessageDialog(null, count);
            res.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        return count;
    }

    public int Numero_estudiantes() {
        int count = 0;

        try {

            Connection cn = conexion();

            PreparedStatement pstm = cn.prepareStatement(" SELECT count(*)"
                    + " FROM estudiante ");
            //Se crea un objeto donde se almacena el resultado
            //Y con el comando executeQuery se ejecuta la consulta en la base de datos
            ResultSet res = pstm.executeQuery();
            //Recorre el resultado para mostrarlo en los jtf
            while (res.next()) {
                //jTF_identificacion.setText(res.getString( "id_persona" ));
                count = (res.getInt("count(*)"));

            }
//            JOptionPane.showMessageDialog(null, count);
            res.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        return count;
    }

    public String[][] Estudiante() {
        String datos[][] = new String[Numero_estudiantes()][4];
        try {

            Connection cn = conexion();

            PreparedStatement pstm = cn.prepareStatement(" SELECT estudiante.id, estudiante.nombre, matricula.codigo,"
                    + "deporte.descripcion from estudiante "
                    + "INNER JOIN matricula ON estudiante.id = matricula.estudiante_id "
                    + "INNER JOIN matricula_deporte on matricula.codigo = matricula_deporte.matricula_codigo "
                    + "INNER JOIN deporte on matricula_deporte.deporte_codigo=deporte.codigo");
            //Se crea un objeto donde se almacena el resultado
            //Y con el comando executeQuery se ejecuta la consulta en la base de datos
            ResultSet res = pstm.executeQuery();
            //Recorre el resultado para mostrarlo en los jtf
            int i = 0;
            while (res.next()) {
                datos[i][0] = (res.getString("id"));
                datos[i][1] = (res.getString("nombre"));
                datos[i][2] = (res.getString("codigo"));
                datos[i][3] = (res.getString("descripcion"));
                i++;
            }
            res.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return datos;
    }

    /**
     * Metodo que permite saber si el estudiante ya esta matriculado
     *
     * @return int cantidad de matriculas
     */
    public int Estudiante_prematriculado(String id) {
        int Count = 0;

        try {

            Connection cn = conexion();

            PreparedStatement pstm = cn.prepareStatement(" SELECT Count(*) "
                    + " FROM matricula  "
                    + " WHERE estudiante_id = '" + id + "'");
            //Se crea un objeto donde se almacena el resultado
            //Y con el comando executeQuery se ejecuta la consulta en la base de datos
            ResultSet res = pstm.executeQuery();
            //Recorre el resultado para mostrarlo en los jtf
            while (res.next()) {
                //jTF_identificacion.setText(res.getString( "id_persona" ));
                Count = (res.getInt("Count(*)"));

            }
//            JOptionPane.showMessageDialog(null, count);
            res.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        return Count;
    }

    public String Nombre_estudiante(String id) {
        String Nombre = "";

        try {

            Connection cn = conexion();

            PreparedStatement pstm = cn.prepareStatement(" SELECT nombre "
                    + " FROM estudiante  "
                    + " WHERE id = '" + id + "'");
            //Se crea un objeto donde se almacena el resultado
            //Y con el comando executeQuery se ejecuta la consulta en la base de datos
            ResultSet res = pstm.executeQuery();
            //Recorre el resultado para mostrarlo en los jtf
            while (res.next()) {
                //jTF_identificacion.setText(res.getString( "id_persona" ));
                Nombre = (res.getString("nombre"));

            }
//            JOptionPane.showMessageDialog(null, count);
            res.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        return Nombre;
    }

    public int Numero_tutores(String id) {
        int Numero = 0;

        try {

            Connection cn = conexion();

            PreparedStatement pstm = cn.prepareStatement(" SELECT COUNT(*) "
                    + " FROM estudiante_tutor  "
                    + " WHERE estudiante_tutor.estudiante_id = '" + id + "'");
            //Se crea un objeto donde se almacena el resultado
            //Y con el comando executeQuery se ejecuta la consulta en la base de datos
            ResultSet res = pstm.executeQuery();
            //Recorre el resultado para mostrarlo en los jtf
            while (res.next()) {
                //jTF_identificacion.setText(res.getString( "id_persona" ));
                Numero = (res.getInt("COUNT(*)"));

            }
//            JOptionPane.showMessageDialog(null, count);
            res.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        return Numero;
    }
    /*
     *SELECT estudiante.*,persoan.id AS Identificaciont,persoan.nombre AS Nombret,persoan.apellido AS apellidoT,tutor.direccion,tutor.parentesco FROM `estudiante` INNER JOIN estudiante_tutor ON estudiante.id=estudiante_tutor.estudiante_id INNER JOIN tutor ON estudiante_tutor.tutor_id=tutor.id INNER JOIN persoan ON tutor.id=persoan.id where estudiante.id=
     */
    public String[][] Datos_estudiante(String id) {
        int Numero = Numero_tutores(id);
        String Datos[][] = new String[11][Numero];

        try {

            Connection cn = conexion();

            PreparedStatement pstm = cn.prepareStatement(" SELECT estudiante.*,"
                    + "persoan.id AS Identificaciont,persoan.nombre AS Nombret,persoan.apellido AS ApellidoT,persoan.telefono,persoan.celular,"
                    + "tutor.direccion,tutor.parentesco "
                    + "FROM `estudiante` "
                    + "INNER JOIN estudiante_tutor ON estudiante.id = estudiante_tutor.estudiante_id "
                    + "INNER JOIN tutor ON estudiante_tutor.tutor_id = tutor.id "
                    + "INNER JOIN persoan ON tutor.id = persoan.id "
                    + "WHERE estudiante.id ='" + id + "'");
            //Se crea un objeto donde se almacena el resultado
            //Y con el comando executeQuery se ejecuta la consulta en la base de datos
            ResultSet res = pstm.executeQuery();
            //Recorre el resultado para mostrarlo en los jtf
            int i = 0;
            while (res.next()) {
                //jTF_identificacion.setText(res.getString( "id_persona" ));

                /* Datos del Estudiante */
                Datos[0][i] = res.getString("id");
                Datos[1][i] = res.getString("nombre");
                Datos[2][i] = res.getString("apellido");
                Datos[3][i] = res.getString("fecha_nacimiento");
                /* Datos del del tutor */
                Datos[4][i] = res.getString("Identificaciont");
                Datos[5][i] = res.getString("Nombret");
                Datos[6][i] = res.getString("ApellidoT");
                Datos[7][i] = res.getString("telefono");
                Datos[8][i] = res.getString("celular");
                Datos[9][i] = res.getString("direccion");
                Datos[10][i] = res.getString("parentesco");

                i++;
            }
//            JOptionPane.showMessageDialog(null, count);
            res.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        return Datos;
    }
    
    public int Numero_matriculas(String id) {
        int Numero = 0;

        try {
            Connection cn = conexion();

            PreparedStatement pstm = cn.prepareStatement(" SELECT COUNT(*) "
                    + " FROM matricula  "
                    + " WHERE estudiante_id = '" + id + "'");
            //Se crea un objeto donde se almacena el resultado
            //Y con el comando executeQuery se ejecuta la consulta en la base de datos
            ResultSet res = pstm.executeQuery();
            //Recorre el resultado para mostrarlo en los jtf
            while (res.next()) {
                //jTF_identificacion.setText(res.getString( "id_persona" ));
                Numero = (res.getInt("COUNT(*)"));

            }
//            JOptionPane.showMessageDialog(null, count);
            res.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        return Numero;
    }

    /*
     getters y setters
     */
    public static String getCorreoF() {
        return correoF;
    }

    public static void setCorreoF(String aCorreoF) {
        correoF = aCorreoF;
    }

}
