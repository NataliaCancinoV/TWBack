package mx.uv;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Data Access Object
public class DAO {
    // en el dao se establece la conexion a la BD
    private static Conexion c = new Conexion();
    // este metodo regresa un conjunto de usuarios que cumpla un criterio
    public static List<Usuario> dameUsuarios() {
        Statement stm = null;
        ResultSet rs = null;
        Connection conn = null;

        List<Usuario> resultado = new ArrayList<>();

        conn = Conexion.getConnection();

        try {
            String sql = "SELECT * from usuarios";
            stm = (Statement) conn.createStatement();
            rs = stm.executeQuery(sql);
            while (rs.next()) {
                Usuario u = new Usuario(rs.getString("id"), rs.getString("correo"), rs.getString("password"), rs.getString("nombre"));
                resultado.add(u);
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println(e);
                }
            rs = null;
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
                stm = null;
            }
            try {
                conn.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        return resultado;
    }

    public static String crearUsuario(Usuario u) {
        PreparedStatement stm = null;
        Connection conn = null;
        String msj = "";

        conn = Conexion.getConnection();
        try {
            String sql = "INSERT INTO usuarios (id, correo, password,nombre) values (?,?,?,?)";
            stm = (PreparedStatement) conn.prepareStatement(sql);
            stm.setString(1, u.getId());
            stm.setString(2, u.getCorreo());
            stm.setString(3, u.getPassword());
            stm.setString(4, u.getNombre());
            if (stm.executeUpdate() > 0)
                msj = "usuario agregado";
            else
                msj = "usuario no agregado";

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
                stm = null;
            }
            try {
                conn.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return msj;
    }

    public static boolean validarUsuario(String correo, String password) {
        PreparedStatement stm = null;
        Connection conn = null;

        conn = Conexion.getConnection();
        try {
            String sql = "SELECT * FROM usuarios WHERE correo = ? AND password = ?";
            stm = conn.prepareStatement(sql);
            stm.setString(1, correo);
            stm.setString(2, password);

            ResultSet rs = stm.executeQuery();

            return rs.next(); // Devuelve true si hay una coincidencia, false si no hay coincidencia

        } catch (Exception e) {
            System.out.println(e);
            return false; // Manejar adecuadamente las excepciones en tu aplicación real
        } finally {
            // Cerrar recursos
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            try {
                conn.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public static boolean correoExistente(String correo) {
        PreparedStatement stm = null;
        Connection conn = null;
    
        conn = Conexion.getConnection();
        try {
            String sql = "SELECT * FROM usuarios WHERE correo = ?";
            stm = conn.prepareStatement(sql);
            stm.setString(1, correo);
    
            ResultSet rs = stm.executeQuery();
    
            return rs.next(); // Devuelve true si hay una coincidencia (correo existente), false si no hay coincidencia
    
        } catch (Exception e) {
            System.out.println(e);
            return false; // Manejar adecuadamente las excepciones en tu aplicación real
        } finally {
            // Cerrar recursos
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            try {
                conn.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public static boolean existeUsuarioPorCorreo(String correo) {
        PreparedStatement stm = null;
        Connection conn = null;

        conn = Conexion.getConnection();
        try {
            String sql = "SELECT COUNT(*) as num_usuarios FROM usuarios WHERE correo = ?";
            stm = conn.prepareStatement(sql);
            stm.setString(1, correo);

            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                return rs.getInt("num_usuarios") > 0; // Devuelve true si existe al menos un usuario con ese correo
            } else {
                return false; // Devuelve false si no hay usuarios con ese correo
            }

        } catch (Exception e) {
            System.out.println(e);
            return false; // Manejar adecuadamente las excepciones en tu aplicación real
        } finally {
            // Cerrar recursos
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            try {
                conn.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public static String cambiarContrasena(String correo, String nuevaContrasena) {
        String mensaje = "";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement stm = conn.prepareStatement("UPDATE usuarios SET password = ? WHERE correo = ?")) {

            // Verificar si el usuario existe
            if (existeUsuarioPorCorreo(correo)) {
                stm.setString(1, nuevaContrasena);
                stm.setString(2, correo);

                if (stm.executeUpdate() > 0) {
                    mensaje = "Contraseña actualizada correctamente";
                } else {
                    mensaje = "No se pudo actualizar la contraseña";
                }
            } else {
                mensaje = "Usuario no encontrado";
            }

        } catch (SQLException e) {
            // Manejo de excepciones (puedes personalizar este manejo según tus necesidades)
            mensaje = "Error al actualizar la contraseña";
            e.printStackTrace(); // O registra la excepción en un sistema de registro
        }

        return mensaje;
    }

    public static String obtenerIdUsuario(String correo, String password) {
        PreparedStatement stm = null;
        Connection conn = null;
    
        conn = Conexion.getConnection();
        try {
            String sql = "SELECT id FROM usuarios WHERE correo = ? AND password = ?";
            stm = conn.prepareStatement(sql);
            stm.setString(1, correo);
            stm.setString(2, password);
    
            ResultSet rs = stm.executeQuery();
    
            if (rs.next()) {
                return rs.getString("id"); // Devuelve el ID si hay una coincidencia
            } else {
                return "falso"; // Devuelve -1 si no hay coincidencia
            }
    
        } catch (Exception e) {
            System.out.println(e);
            return "falso"; // Manejar adecuadamente las excepciones en tu aplicación real
        } finally {
            // Cerrar recursos
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            try {
                conn.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    

    public static Usuario obtenerDatosUsuario(String id) {
        PreparedStatement stm = null;
        Connection conn = null;

        conn = Conexion.getConnection();
        try {
            String sql = "SELECT * FROM usuarios WHERE id = ?";
            stm = conn.prepareStatement(sql);
            stm.setString(1, id);

            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                // Crear un objeto Usuario con los datos del usuario
                Usuario usuario = new Usuario();
                usuario.setId(rs.getString("id"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setPassword(rs.getString("password"));
                usuario.setNombre(rs.getString("nombre"));
                // Agregar más campos según tu estructura de base de datos

                return usuario;
            } else {
                return null; // Devuelve null si no hay coincidencia
            }

        } catch (Exception e) {
            System.out.println(e);
            return null; // Manejar adecuadamente las excepciones en tu aplicación real
        } finally {
            // Cerrar recursos
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            try {
                conn.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }



    public static String hacerPedido(Pasteles pasteles) {
        PreparedStatement stm = null;
        Connection conn = null;
        String mensaje = "";
    
        conn = Conexion.getConnection();
        try {
            String sql = "INSERT INTO pedidos (idUsuario, idPastel, precio, tamaño, estatus, inscripcion, relleno) VALUES (?,?,?,?,?,?,?)";
            stm = conn.prepareStatement(sql);
            stm.setString(1, pasteles.getIdUsuario());
            stm.setString(2, pasteles.getIdPastel());
            stm.setString(3, pasteles.getIdPrecio());
            stm.setString(4, pasteles.getIdTamaño());
            stm.setString(5, pasteles.getStatus());
            stm.setString(6, pasteles.getInscripcion());
            stm.setString(7, pasteles.getTipoRelleno());
    
            if (stm.executeUpdate() > 0)
                mensaje = "Pedido realizado con éxito";
            else
                mensaje = "No se pudo realizar el pedido";
    
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException e) {
                    System.out.println("Error al cerrar el statement: " + e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Error al cerrar la conexión: " + e.getMessage());
                }
            }
        }
        return mensaje;
    }

    public static List<Pasteles> damePedidosDePastelesPorUsuario(String idUsuario) {
        System.out.println("ENTRO AL METODO: damePedidosDePastelesPorUsuario");
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Pasteles> resultado = new ArrayList<>();

        try {
            conn = Conexion.getConnection();
            String sql = "SELECT p.idPedido, p.idUsuario, p.idPastel, pastel.nombreP as nombre_pastel, pastel.precio, "
                    + "p.tamaño, p.estatus, p.inscripcion, p.relleno "
                    + "FROM pedidos p "
                    + "JOIN pasteles pastel ON p.idPastel = pastel.id "
                    + "WHERE p.idUsuario = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, idUsuario);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Pasteles pastel = new Pasteles(
                        rs.getString("idPedido"),
                        rs.getString("idUsuario"),
                        rs.getString("idPastel"),
                        rs.getString("nombre_pastel"),
                        rs.getString("precio"),
                        rs.getString("tamaño"),
                        rs.getString("estatus"),
                        rs.getString("inscripcion"),
                        rs.getString("relleno")
                );
                resultado.add(pastel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return resultado;
    
}

public static List<Pasteles> dameTodosLosPedidosDePasteles2() {
    System.out.println("ENTRO AL METODO: dameTodosLosPedidosDePasteles");
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    List<Pasteles> resultado = new ArrayList<>();

    try {
        conn = Conexion.getConnection();
        String sql = "SELECT p.idPedido, p.idUsuario, p.idPastel, pastel.nombreP as nombre_pastel, pastel.precio, "
                + "p.tamaño, p.estatus, p.inscripcion, p.relleno "
                + "FROM pedidos p "
                + "JOIN pasteles pastel ON p.idPastel = pastel.id";
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();
        while (rs.next()) {
            Pasteles pastel = new Pasteles(
                    rs.getString("idPedido"),
                    rs.getString("idUsuario"),
                    rs.getString("idPastel"),
                    rs.getString("nombre_pastel"),
                    rs.getString("precio"),
                    rs.getString("tamaño"),
                    rs.getString("estatus"),
                    rs.getString("inscripcion"),
                    rs.getString("relleno")
            );
            resultado.add(pastel);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    return resultado;
}

public static String agregarReseña(Reseñas reseña) {
    PreparedStatement stm = null;
    Connection conn = null;
    String mensaje = "";

    conn = Conexion.getConnection();
    try {
        String sql = "INSERT INTO reseñas (nombreUsuario, idPastel, contenido, estrellas) VALUES (?,?,?,?)";
        stm = conn.prepareStatement(sql);
        stm.setString(1, reseña.getNombreUsuario());
        stm.setString(2, reseña.getIdPastel());
        stm.setString(3, reseña.getContenido());
        stm.setInt(4, reseña.getEstrellas());

        if (stm.executeUpdate() > 0)
            mensaje = "Reseña agregada con éxito";
        else
            mensaje = "No se pudo agregar la reseña";

    } catch (SQLException e) {
        System.out.println("Error al ejecutar la consulta: " + e.getMessage());
    } finally {
        if (stm != null) {
            try {
                stm.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar el statement: " + e.getMessage());
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
    return mensaje;
}

public static List<Reseñas> obtenerReseñasPorPastel(String idPastel) {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    List<Reseñas> resultado = new ArrayList<>();

    try {
        conn = Conexion.getConnection();
        String sql = "SELECT r.idReseña, r.nombreUsuario, r.idPastel, r.contenido, r.estrellas "
                    + "FROM reseñas r "
                    + "WHERE r.idPastel = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, idPastel);
        rs = pstmt.executeQuery();
        while (rs.next()) {
            Reseñas reseña = new Reseñas(
                rs.getString("idReseña"),
                rs.getString("nombreUsuario"),
                rs.getString("idPastel"),
                rs.getString("contenido"),
                rs.getInt("estrellas")
            );
            resultado.add(reseña);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    return resultado;
}


public static boolean eliminarPedido(String idPedido) {
    System.out.println("ENTRO AL METODO: eliminarPedido");
    Statement stm = null;
    Connection conn = null;
    
    conn = Conexion.getConnection();

    try {
        String sql = "DELETE FROM pedidos WHERE idPedido = '" + idPedido + "'";
        stm = (Statement) conn.createStatement();
        int filasAfectadas = stm.executeUpdate(sql);

        // Verificamos si se eliminó alguna fila
        if (filasAfectadas > 0) {
            System.out.println("Pedido eliminado exitosamente.");
            return true;
        } else {
            System.out.println("No se encontró ningún pedido con el ID especificado.");
            return false;
        }
    } catch (SQLException e) {
        System.out.println("Error al eliminar el pedido: " + e);
    } finally {
        // Cierre de recursos
        if (stm != null) {
            try {
                stm.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }

    return false;
}


public static boolean actualizarEstatusPedido(String idPedido, String nuevoEstatus) {
    System.out.println("ENTRO AL METODO: actualizarEstatusPedido");
    Statement stm = null;
    Connection conn = null;
    
    conn = Conexion.getConnection();

    try {
        String sql = "UPDATE pedidos SET estatus = '" + nuevoEstatus + "' WHERE idPedido = '" + idPedido + "'";
        stm = (Statement) conn.createStatement();
        int filasAfectadas = stm.executeUpdate(sql);

        // Verificamos si se actualizó alguna fila
        if (filasAfectadas > 0) {
            System.out.println("Pedido actualizado exitosamente.");
            return true;
        } else {
            System.out.println("No se encontró ningún pedido con el ID especificado.");
            return false;
        }
    } catch (SQLException e) {
        System.out.println("Error al actualizar el pedido: " + e);
    } finally {
        // Cierre de recursos
        if (stm != null) {
            try {
                stm.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    return false;
}


    


    
}