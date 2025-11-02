
package com.myconstruction.controlador;

import java.io.IOException;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

// Anotación para mapear la URL: http://.../LoginServlet
@WebServlet("/LoginServlet") 
public class LoginServlet extends HttpServlet {
    
    // --- Configuración de la Conexión a la Base de Datos ---
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/myconstruction_db";
    private static final String JDBC_USER = "root"; // ¡Ajusta tu usuario de DB!
    private static final String JDBC_PASSWORD = "your_db_password"; // ¡Ajusta tu contraseña de DB!

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Obtener parámetros del formulario
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        try {
            // Cargar el driver JDBC (no siempre es necesario en versiones modernas, pero es buena práctica)
            // Class.forName("com.mysql.cj.jdbc.Driver"); 
            
            // 2. Establecer la conexión con la DB
            try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
                
                // 3. Preparar la consulta SQL (uso de PreparedStatement para seguridad)
                String sql = "SELECT nombre FROM usuarios WHERE username = ? AND password_hash = ?";
                
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, username);
                    stmt.setString(2, password); 
                    
                    // 4. Ejecutar la consulta
                    try (ResultSet rs = stmt.executeQuery()) {
                        
                        if (rs.next()) {
                            // 5. Autenticación Exitosa
                            String nombreUsuario = rs.getString("nombre");
                            
                            // Crear una Sesión HTTP
                            HttpSession session = request.getSession();
                            session.setAttribute("user", nombreUsuario); // Guardamos el nombre para usarlo
                            
                            // Redirigir al panel principal
                            response.sendRedirect("home.jsp"); 
                        } else {
                            // 6. Autenticación Fallida
                            request.setAttribute("error", "Credenciales inválidas. Intente de nuevo.");
                            // Volver al login.jsp manteniendo el mensaje de error
                            request.getRequestDispatcher("login.jsp").forward(request, response);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error de conexión o de base de datos.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
        // Nota: Se podría añadir un Servlet para cerrar sesión (LogoutServlet)
    }
}