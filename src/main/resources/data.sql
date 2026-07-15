-- 1. Roles y Categorías (Base)
INSERT INTO roles (id, nombre) VALUES (1, 'ROLE_ADMIN');
INSERT INTO roles (id, nombre) VALUES (2, 'ROLE_CLIENTE');

INSERT INTO categorias (id, nombre, descripcion) VALUES (1, 'Electronica', 'Dispositivos de computo');
INSERT INTO categorias (id, nombre, descripcion) VALUES (2, 'Accesorios', 'Perifericos y cables');

-- 2. Almacenes y Proveedores
INSERT INTO almacenes (id, nombre, ubicacion, capacidad_maxima) VALUES (1, 'Almacen Principal', 'Ate, Lima', 1000);
INSERT INTO proveedores (id, ruc, razon_social, telefono, correo) VALUES (1, '20123456789', 'TechSupply S.A.', '999888777', 'ventas@techsupply.com');

-- 3. Clientes y Empleados (Basados en tu herencia de Persona)
INSERT INTO clientes (id, nombres, apellidos, dni, correo, telefono, ruc, direccion_entrega)
VALUES (1, 'Juan', 'Perez', '12345678', 'juan@gmail.com', '987654321', '10123456789', 'Av. La Molina 123');

INSERT INTO empleados (id, nombres, apellidos, dni, correo, telefono, cargo, salario)
VALUES (1, 'Ana', 'Lopez', '87654321', 'ana@empresa.com', '912345678', 'VENDEDOR', 2500.00);

-- 4. Usuarios (Vinculados a Roles)
INSERT INTO usuarios (id, username, password, rol_id, persona_id) VALUES (1, 'admin', '123456', 1, 1);
INSERT INTO usuarios (id, username, password, rol_id, persona_id) VALUES (2, 'juanp', 'password', 2, 1);

-- 5. Productos (Dependen de Categorias)
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria_id) VALUES (1, 'Laptop Gamer', 'RTX 3060, 16GB RAM', 4500.00, 10, 1);
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria_id) VALUES (2, 'Mouse Inalambrico', 'Logitech G305', 150.00, 50, 2);

-- 6. Inventarios (Dependen de Productos y Almacenes)
INSERT INTO inventarios (id, producto_id, almacen_id, cantidad_fisica, ultima_actualizacion) VALUES (1, 1, 1, 10, CURRENT_TIMESTAMP);
INSERT INTO inventarios (id, producto_id, almacen_id, cantidad_fisica, ultima_actualizacion) VALUES (2, 2, 1, 50, CURRENT_TIMESTAMP);

-- 7. Pedidos (Dependen de Clientes)
INSERT INTO pedidos (id, fecha_pedido, total, estado, cliente_id) VALUES (1, CURRENT_TIMESTAMP, 4650.00, 'PENDIENTE', 1);

-- 8. Detalles Pedido (Dependen de Pedido y Producto)
INSERT INTO detalles_pedido (id, cantidad, precio_unitario, subtotal, pedido_id, producto_id) VALUES (1, 1, 4500.00, 4500.00, 1, 1);
INSERT INTO detalles_pedido (id, cantidad, precio_unitario, subtotal, pedido_id, producto_id) VALUES (2, 1, 150.00, 150.00, 1, 2);

-- 9. Pagos (Dependen de Pedido)
-- INSERT INTO pagos (id, monto, fecha_pago, metodo, pedido_id) VALUES (1, 4650.00, CURRENT_TIMESTAMP, 'TARJETA', 1);

-- 10. Comprobantes (Dependen de Pedido)
-- INSERT INTO comprobantes (id, numero_emision, tipo_documento, fecha_emision, total, pedido_id) VALUES (1, 'F001-00000001', 'FACTURA', CURRENT_TIMESTAMP, 4650.00, 1);

-- 11. Envios (Dependen de Pedido)
INSERT INTO envios (id, direccion_destino, fecha_despacho, fecha_entrega_estimada, estado, pedido_id)
VALUES (1, 'Av. La Molina 123', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'PREPARANDO', 1);

-- 12. Devoluciones y Notificaciones (Opcional para test)
INSERT INTO devoluciones (id, fecha_devolucion, motivo, estado, pedido_id) VALUES (1, CURRENT_TIMESTAMP, 'Ninguno', 'APROBADA', 1);
INSERT INTO notificaciones (id, mensaje, tipo, fecha_envio, usuario_id) VALUES (1, 'Bienvenido al sistema', 'SISTEMA', CURRENT_TIMESTAMP, 1);