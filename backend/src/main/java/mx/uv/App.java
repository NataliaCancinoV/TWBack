package mx.uv;

/**
 * Hello world!
 *
 */

import static spark.Spark.*;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import com.google.gson.*;

public class App 
{
    static Gson gson = new Gson();
    static HashMap<String, Usuario> usuarios = new HashMap<String, Usuario>();
    static String correoG;
    static String passwordG;
    static String nombreG;
    static String idG;
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );



        //port(80);
        port(getHerokuAssignedPort());

        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });
        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

        get("/backend/verificar-conexion", (request, response) -> {
            response.type("application/json");
            
            JsonObject respuesta = new JsonObject();
            respuesta.addProperty("mensaje", "Conexión exitosa al backend");
            
            return respuesta.toString();
        });

        post("/frontend/", (request, response)->{
            response.type("application/json");
            String payload = request.body();
            JsonElement jsonElement = JsonParser.parseString(payload);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Usuario usuario = gson.fromJson(jsonObject.get("datosFormulario"), Usuario.class);
            System.out.println("usuario"+usuario);
            System.out.println("payload "+payload);
            String id = UUID.randomUUID().toString();
            usuario.setId(id);
            usuarios.put(id, usuario);
            DAO.crearUsuario(usuario);
            System.out.println("i "+usuario.getId());
            System.out.println("n "+usuario.getCorreo());
            System.out.println("p "+usuario.getPassword());
            JsonObject respuesta = new JsonObject();
            respuesta.addProperty("msj", "Se creo el usuario");
            respuesta.addProperty("id", id);
            return gson.toJson(usuario);
        });

        post("/frontend/correoExiste", (request, response) -> {
            response.type("application/json");
        
            // Obtener datos del formulario enviado
            String payload = request.body();
    
            // Parsear el cuerpo JSON
            JsonObject jsonObject = JsonParser.parseString(payload).getAsJsonObject();
        
            // Acceder a la clave "datosFormulario" y luego obtener la clave "correo"
            String correo = jsonObject.getAsJsonObject("datosFormulario").get("correo").getAsString();
            
            System.out.println("correo: "+correo);
            // Verificar si el correo existe
            boolean correoExistente = DAO.correoExistente(correo);
        
            // Construir un objeto JSON con la información sobre si el correo existe
            JsonObject resultadoJson = new JsonObject();
            resultadoJson.addProperty("correoExistente", correoExistente);
        
            return resultadoJson.toString();
        });

        post("/frontend/obtenerUsuario", (request, response) -> {
            response.type("application/json");
        
            // Puedes acceder a las variables globales directamente o utilizar métodos getter según tu implementación
            String correo = correoG;
            String password = passwordG;
            String nombre = nombreG;
        
            // Construir un objeto JSON con los datos del usuario
            JsonObject usuarioJson = new JsonObject();
            usuarioJson.addProperty("correo", correo);
            usuarioJson.addProperty("password", password);
            usuarioJson.addProperty("nombre", nombre);
            System.out.println(nombre);;
            System.out.println(usuarioJson);
            return usuarioJson.toString();
        });

        post("/frontend/login", (request, response)->{
            response.type("application/json");
            String payload = request.body();
            System.out.println("payload "+payload);
            // DAO.crearUsuario(usuario);

            String correo = "";
            String password = "";

            JsonElement jsonElement = JsonParser.parseString(payload);
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            // Accede a la clave "datosFormulario" y luego obtén las claves "correo" y "password"
            JsonObject datosFormulario = jsonObject.getAsJsonObject("datosFormulario");

            if (datosFormulario != null && datosFormulario.has("correo") && datosFormulario.has("password")) {
                correo = datosFormulario.get("correo").getAsString();
                password = datosFormulario.get("password").getAsString();
                System.out.println("Correogson: " + correo);
                System.out.println("Passwordgson: " + password);
            } else {
                System.out.println("Las claves 'correo' y/o 'password' no están presentes en el JSON.");
            }
            boolean esUsuarioValido = DAO.validarUsuario(correo, password);
            JsonObject respuesta = new JsonObject();
            if (esUsuarioValido) {
            correoG = correo;
            passwordG = password;
            String id = DAO.obtenerIdUsuario(correoG,passwordG);
            idG = id;
            System.out.println("correo valido "+correoG);
            System.out.println("password valido "+passwordG);
            System.out.println("id valido "+passwordG);
            
            
            Usuario usuario = DAO.obtenerDatosUsuario(id);
            nombreG = usuario.getNombre();
            System.out.println("nombre valido: "+nombreG);
            respuesta.addProperty("msj", "Valido");
            respuesta.addProperty("nombre", usuario.getNombre());
            respuesta.addProperty("id", id);
            return gson.toJson(usuario);
            } else {
                respuesta.addProperty("msj", "Invalido");
                return "Invalido";
            }
        });

        post("/Login", (request, response) -> {
            response.type("application/json");
        
            // Puedes acceder a las variables globales directamente o utilizar métodos getter según tu implementación
            String correo = correoG;
            String password = passwordG;
            String nombre = nombreG;
        
            // Construir un objeto JSON con los datos del usuario
            JsonObject usuarioJson = new JsonObject();
            usuarioJson.addProperty("correo", correo);
            usuarioJson.addProperty("password", password);
            usuarioJson.addProperty("nombre", nombre);
            System.out.println(nombre);;
            System.out.println(usuarioJson);
            return usuarioJson.toString();
        });


        post("/frontend/cerrarSesion", (request, response) -> {
            response.type("application/json");
        
            // Establecer las variables a null
            correoG = null;
            passwordG = null;
            nombreG = null;
            idG = null;

            String correo = correoG;
            String password = passwordG;
            String nombre = nombreG;
        
            // Construir un objeto JSON con los datos del usuario
            JsonObject usuarioJson = new JsonObject();
            usuarioJson.addProperty("correo", correo);
            usuarioJson.addProperty("password", password);
            usuarioJson.addProperty("nombre", nombre);
            usuarioJson.addProperty("id", idG);
        
            System.out.println(usuarioJson);
            return usuarioJson.toString();
        });


        post("/frontend/login", (request, response)->{
            response.type("application/json");
            String payload = request.body();
            System.out.println("payload "+payload);
            // DAO.crearUsuario(usuario);

            String correo = "";
            String password = "";

            JsonElement jsonElement = JsonParser.parseString(payload);
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            // Accede a la clave "datosFormulario" y luego obtén las claves "correo" y "password"
            JsonObject datosFormulario = jsonObject.getAsJsonObject("datosFormulario");

            if (datosFormulario != null && datosFormulario.has("correo") && datosFormulario.has("password")) {
                correo = datosFormulario.get("correo").getAsString();
                password = datosFormulario.get("password").getAsString();
                System.out.println("Correogson: " + correo);
                System.out.println("Passwordgson: " + password);
            } else {
                System.out.println("Las claves 'correo' y/o 'password' no están presentes en el JSON.");
            }
            boolean esUsuarioValido = DAO.validarUsuario(correo, password);
            JsonObject respuesta = new JsonObject();
            if (esUsuarioValido) {
            correoG = correo;
            passwordG = password;
            String id = DAO.obtenerIdUsuario(correoG,passwordG);
            idG = id;
            System.out.println("correo valido "+correoG);
            System.out.println("password valido "+passwordG);
            System.out.println("id valido "+passwordG);
            
            
            Usuario usuario = DAO.obtenerDatosUsuario(id);
            nombreG = usuario.getNombre();
            System.out.println("nombre valido: "+nombreG);
            respuesta.addProperty("msj", "Valido");
            respuesta.addProperty("nombre", usuario.getNombre());
            respuesta.addProperty("id", id);
            return gson.toJson(usuario);
            } else {
                respuesta.addProperty("msj", "Invalido");
                return "Invalido";
            }
        });


        //Recuperar Contraseña:

         post("/frontend/RecuperarContra", (request, response) -> {
            response.type("application/json");
            String payload = request.body();
            System.out.println(payload);

            String correo = "";
            String password = "";

            JsonElement jsonElement = JsonParser.parseString(payload);
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            JsonObject datosFormulario = jsonObject.getAsJsonObject("datosFormulario");

            if (datosFormulario != null && datosFormulario.has("correo") && datosFormulario.has("password")) {
                correo = datosFormulario.get("correo").getAsString();
                password = datosFormulario.get("password").getAsString();
                System.out.println("Correo: " + correo);
                System.out.println("Password: " + password);
            } else {
                System.out.println("Las claves 'correo' y/o 'password' no están presentes en el JSON.");
            }
            boolean existeUsuario = DAO.existeUsuarioPorCorreo(correo);
            JsonObject respuesta = new JsonObject();
            if (existeUsuario) {

                respuesta.addProperty("msj", "Usuario encontrado");
                return "Usuario encontrado";
            } else {
                respuesta.addProperty("msj", "Usuario no encontrado");
                return "Usuario no encontrado";
            }
        });

        post("/frontend/ColocarContra2", (request, response) -> {
            response.type("application/json");
            String payload = request.body();

            String correo = "";
            String password = "";

            JsonElement jsonElement = JsonParser.parseString(payload);
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            JsonObject datosFormulario = jsonObject.getAsJsonObject("datosFormulario");

            if (datosFormulario != null && datosFormulario.has("correo") && datosFormulario.has("password")) {
                correo = datosFormulario.get("correo").getAsString();
                password = datosFormulario.get("password").getAsString();
                System.out.println("Correo: " + correo);
                System.out.println("Password: " + password);
            } else {
                System.out.println("Las claves 'correo' y/o 'password' no están presentes en el JSON.");
            }
            System.out.println(correo);
            System.out.println(password);
            DAO.cambiarContrasena(correo, password);

            System.out.println(payload);
            return "Actualizado";
        });

        post("/frontend/RecuperarContra", (request, response) -> {
            response.type("application/json");
            String payload = request.body();
            System.out.println(payload);

            String correo = "";
            String password = "";

            JsonElement jsonElement = JsonParser.parseString(payload);
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            JsonObject datosFormulario = jsonObject.getAsJsonObject("datosFormulario");

            if (datosFormulario != null && datosFormulario.has("correo") && datosFormulario.has("password")) {
                correo = datosFormulario.get("correo").getAsString();
                password = datosFormulario.get("password").getAsString();
                System.out.println("Correo: " + correo);
                System.out.println("Password: " + password);
            } else {
                System.out.println("Las claves 'correo' y/o 'password' no están presentes en el JSON.");
            }
            boolean existeUsuario = DAO.existeUsuarioPorCorreo(correo);
            JsonObject respuesta = new JsonObject();
            if (existeUsuario) {

                respuesta.addProperty("msj", "Usuario encontrado");
                return "Usuario encontrado";
            } else {
                respuesta.addProperty("msj", "Usuario no encontrado");
                return "Usuario no encontrado";
            }
        });



                //Hacer Reservaciones:

                post("/frontend/hacerPedidoPastel1", (request, response) -> {
                    response.type("application/json");
                    String payload = request.body();
                    
                    try {
                        JsonElement jsonElement = JsonParser.parseString(payload);
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        
                       // Obtener los datos del pastel del JSON recibido
                       String textoEncima = jsonObject.get("textoEncima").getAsString();
                       String textoCantidad = jsonObject.get("textoCantidad").getAsString();
                       String textoRelleno = jsonObject.get("textoRelleno").getAsString();
                       String textoTipo = jsonObject.get("textoTipo").getAsString();
                       String textoPrecio = jsonObject.get("textoPrecio").getAsString();
                       String textoId = jsonObject.get("idPastel").getAsString();

                       System.out.println("tamaño: " + textoCantidad);
                       System.out.println("tipo: " + textoTipo);

               
                       // Crear un nuevo objeto Pasteles y asignar los valores recibidos
                       Pasteles reservacion = new Pasteles();
                       reservacion.setIdPedido(UUID.randomUUID().toString());
                       reservacion.setIdUsuario(idG); // Asignar el ID del usuario (¿De dónde obtienes idG?)
                       reservacion.setIdPastel(textoId); // Asignar el ID del pastel (¿De dónde obtienes este valor?)
                       reservacion.setIdNombre(textoTipo); // Asignar el nombre recibido desde el frontend
                       reservacion.setIdPrecio(textoPrecio); // Asignar el precio del pastel (¿De dónde obtienes este valor?)
                       reservacion.setIdTamaño(textoCantidad); // Asignar el tamaño recibido desde el frontend
                       reservacion.setStatus("en proceso"); // Asignar el estado "en proceso"
                       reservacion.setInscripcion(textoEncima); // Asignar la inscripción recibida desde el frontend
                       reservacion.setTipoRelleno(textoRelleno); // Asignar el tipo de relleno recibido desde el frontend
                        
                        // Puedes realizar acciones adicionales con la información de la reservación
                System.out.println("Reservación: " + reservacion);

                        // Lógica para hacer la reservación en la base de datos
                        String mensaje = DAO.hacerPedido(reservacion);
                
                        // Crear la respuesta
                        JsonObject respuesta = new JsonObject();
                        respuesta.addProperty("msj", mensaje);
                
                        return gson.toJson(respuesta);
                    } catch (JsonSyntaxException e) {
                        // Manejar errores de formato JSON
                        System.out.println("Error en el formato JSON: " + e.getMessage());
                        response.status(400); // Bad Request
                        return gson.toJson("Error en el formato JSON");
                    } catch (Exception e) {
                        // Manejar otros errores
                        System.out.println("Error en la reservación: " + e.getMessage());
                        response.status(500); // Internal Server Error
                        return gson.toJson("Error en la reservación");
                    }
                });

                post("/frontend/obtenerPedidosDePasteles", (request, response) -> {
                    response.type("application/json");
                
                    // Obtener el ID del usuario desde la variable global
                    String idUsuario = idG;
                
                    // Obtener los pedidos de pasteles para el usuario
                    List<Pasteles> pedidosDePasteles = DAO.damePedidosDePastelesPorUsuario(idUsuario);
                
                    int numeroDePedidos = pedidosDePasteles.size();
                
                    System.out.println("Número de pedidos de pasteles: " + numeroDePedidos);
                
                    // Construir un objeto JSON con los pedidos de pasteles
                    JsonArray pedidosArray = new JsonArray();
                    for (Pasteles pedido : pedidosDePasteles) {
                        JsonObject pedidoJson = new JsonObject();
                        pedidoJson.addProperty("id_pedido", pedido.getIdPedido());
                        pedidoJson.addProperty("id_usuario", pedido.getIdUsuario());
                        pedidoJson.addProperty("id_pastel", pedido.getIdPastel());
                        pedidoJson.addProperty("nombre_pastel", pedido.getIdNombre());
                        pedidoJson.addProperty("precio", pedido.getIdPrecio());
                        pedidoJson.addProperty("tamaño", pedido.getIdTamaño());
                        pedidoJson.addProperty("estatus", pedido.getStatus());
                        pedidoJson.addProperty("inscripcion", pedido.getInscripcion());
                        pedidoJson.addProperty("relleno", pedido.getTipoRelleno());
                        pedidosArray.add(pedidoJson);
                    }
                
                    // Crear el objeto final que contiene todos los pedidos de pasteles
                    JsonObject responseJson = new JsonObject();
                    responseJson.add("pedidos_de_pasteles", pedidosArray);
                
                    System.out.println(responseJson);
                    return responseJson.toString();
                });

                post("/frontend/eliminarPedido", (request, response) -> {
                    response.type("application/json");
                    String payload = request.body();
                    JsonElement jsonElement = JsonParser.parseString(payload);
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    String idPedido = jsonObject.get("datosId").getAsJsonObject().get("idPedido").getAsString();
                
                    // Lógica para eliminar el pedido usando el ID
                    boolean eliminado = DAO.eliminarPedido(idPedido);
                
                    JsonObject respuesta = new JsonObject();
                    if (eliminado) {
                        respuesta.addProperty("msj", "Pedido eliminado exitosamente.");
                    } else {
                        respuesta.addProperty("msj", "No se encontró ningún pedido con el ID especificado.");
                    }
                
                    return respuesta.toString();
                });

                post("/frontend/actualizarEstatusPedido", (request, response) -> {
                    response.type("application/json");
                    String payload = request.body();
                    JsonElement jsonElement = JsonParser.parseString(payload);
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    String idPedido = jsonObject.get("datosId").getAsJsonObject().get("idPedido").getAsString();
                    String nuevoEstatus = jsonObject.get("datosId").getAsJsonObject().get("nuevoEstatus").getAsString();
                
                    // Lógica para actualizar el estatus del pedido usando el ID
                    boolean actualizado = DAO.actualizarEstatusPedido(idPedido, nuevoEstatus);
                
                    JsonObject respuesta = new JsonObject();
                    if (actualizado) {
                        respuesta.addProperty("msj", "Pedido actualizado exitosamente.");
                    } else {
                        respuesta.addProperty("msj", "No se encontró ningún pedido con el ID especificado.");
                    }
                
                    return respuesta.toString();
                });
                
                
                post("/frontend/obtenerPedidosDePastelesAdmin", (request, response) -> {
                    response.type("application/json");

                    // Obtener todos los pedidos de pasteles
                    List<Pasteles> pedidosDePasteles = DAO.dameTodosLosPedidosDePasteles2();
                
                    int numeroDePedidos = pedidosDePasteles.size();
                
                    System.out.println("Número de pedidos de pasteles: " + numeroDePedidos);
                
                    // Construir un objeto JSON con los pedidos de pasteles
                    JsonArray pedidosArray = new JsonArray();
                    for (Pasteles pedido : pedidosDePasteles) {
                        JsonObject pedidoJson = new JsonObject();
                        pedidoJson.addProperty("id_pedido", pedido.getIdPedido());
                        pedidoJson.addProperty("id_usuario", pedido.getIdUsuario());
                        pedidoJson.addProperty("id_pastel", pedido.getIdPastel());
                        pedidoJson.addProperty("nombre_pastel", pedido.getIdNombre());
                        pedidoJson.addProperty("precio", pedido.getIdPrecio());
                        pedidoJson.addProperty("tamaño", pedido.getIdTamaño());
                        pedidoJson.addProperty("estatus", pedido.getStatus());
                        pedidoJson.addProperty("inscripcion", pedido.getInscripcion());
                        pedidoJson.addProperty("relleno", pedido.getTipoRelleno());
                        pedidosArray.add(pedidoJson);
                    }
                
                    // Crear el objeto final que contiene todos los pedidos de pasteles
                    JsonObject responseJson = new JsonObject();
                    responseJson.add("pedidos_de_pasteles", pedidosArray);
                
                    System.out.println(responseJson);
                    return responseJson.toString();
                });

                post("/frontend/agregarResenia", (request, response) -> {
                    response.type("application/json");
                    String payload = request.body();
                
                    try {
                        JsonElement jsonElement = JsonParser.parseString(payload);
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                
                        // Obtener los datos de la reseña del JSON recibido
                        String nombreUsuario = jsonObject.get("nombreUsuario").getAsString();
                        String idPastel = jsonObject.get("idPastel").getAsString();
                        String contenido = jsonObject.get("contenido").getAsString();
                        int estrellas = jsonObject.get("estrellas").getAsInt();
                
                        // Crear un nuevo objeto Reseñas y asignar los valores recibidos
                        Reseñas reseña = new Reseñas();
                        reseña.setIdReseña(UUID.randomUUID().toString());
                        reseña.setNombreUsuario(nombreUsuario);
                        reseña.setIdPastel(idPastel);
                        reseña.setContenido(contenido);
                        reseña.setEstrellas(estrellas);
                
                        // Puedes realizar acciones adicionales con la información de la reseña
                        System.out.println("Reseña: " + reseña);
                
                        // Lógica para agregar la reseña en la base de datos
                        String mensaje = DAO.agregarReseña(reseña);
                
                        // Crear la respuesta
                        JsonObject respuesta = new JsonObject();
                        respuesta.addProperty("msj", mensaje);
                
                        return gson.toJson(respuesta);
                    } catch (JsonSyntaxException e) {
                        // Manejar errores de formato JSON
                        System.out.println("Error en el formato JSON: " + e.getMessage());
                        response.status(400); // Bad Request
                        return gson.toJson("Error en el formato JSON");
                    } catch (Exception e) {
                        // Manejar otros errores
                        System.out.println("Error al agregar la reseña: " + e.getMessage());
                        response.status(500); // Internal Server Error
                        return gson.toJson("Error al agregar la reseña");
                    }
                });

                
                post("/frontend/obtenerReseniasPorPastel", (request, response) -> {
                    response.type("application/json");
                
                    // Obtener el ID del pastel desde el cuerpo de la solicitud
                    JsonObject requestBody = new Gson().fromJson(request.body(), JsonObject.class);
                    String idPastel = requestBody.get("idPastel").getAsString();
                
                    // Obtener las reseñas para el pastel
                    List<Reseñas> reseñas = DAO.obtenerReseñasPorPastel(idPastel);

                    System.out.println("ID del pastel"+idPastel);
                
                    int numeroDeReseñas = reseñas.size();
                
                    System.out.println("Número de reseñas: " + numeroDeReseñas);
                
                    // Construir un objeto JSON con las reseñas
                    JsonArray reseñasArray = new JsonArray();
                    for (Reseñas reseña : reseñas) {
                        JsonObject reseñaJson = new JsonObject();
                        reseñaJson.addProperty("id_reseña", reseña.getIdReseña());
                        reseñaJson.addProperty("nombre_usuario", reseña.getNombreUsuario());
                        reseñaJson.addProperty("id_pastel", reseña.getIdPastel());
                        reseñaJson.addProperty("contenido", reseña.getContenido());
                        reseñaJson.addProperty("estrellas", reseña.getEstrellas());
                        reseñasArray.add(reseñaJson);
                    }
                
                    // Crear el objeto final que contiene todas las reseñas
                    JsonObject responseJson = new JsonObject();
                    responseJson.add("reseñas", reseñasArray);
                
                    System.out.println(responseJson);
                    return responseJson.toString();
                });
                // Puerto donde se ejecutará el servidor
                ChatServer server = new ChatServer(1234);
                server.start();
                System.out.println("Chat server started on port: " + server.getPort());

    }



    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}