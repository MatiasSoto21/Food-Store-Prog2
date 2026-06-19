CREATE DATABASE IF NOT EXISTS food_store_db;
USE food_store_db;

DROP TABLE IF EXISTS detalle_pedido;
DROP TABLE IF EXISTS pedido;
DROP TABLE IF EXISTS producto;
DROP TABLE IF EXISTS usuario;
DROP TABLE IF EXISTS categoria;

CREATE TABLE categoria (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           nombre VARCHAR(100) NOT NULL UNIQUE,
                           descripcion VARCHAR(255),
                           eliminado BOOLEAN NOT NULL DEFAULT FALSE,
                           created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE producto (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          nombre VARCHAR(100) NOT NULL,
                          precio DECIMAL(10,2) NOT NULL,
                          descripcion VARCHAR(255),
                          stock INT NOT NULL,
                          imagen VARCHAR(255),
                          disponible BOOLEAN NOT NULL DEFAULT TRUE,
                          id_categoria BIGINT NOT NULL,
                          eliminado BOOLEAN NOT NULL DEFAULT FALSE,
                          created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

                          CONSTRAINT fk_producto_categoria
                              FOREIGN KEY (id_categoria)
                                  REFERENCES categoria(id),

                          CONSTRAINT chk_producto_precio
                              CHECK (precio >= 0),

                          CONSTRAINT chk_producto_stock
                              CHECK (stock >= 0)
);

CREATE TABLE usuario (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         nombre VARCHAR(100) NOT NULL,
                         apellido VARCHAR(100) NOT NULL,
                         mail VARCHAR(150) NOT NULL UNIQUE,
                         celular VARCHAR(50),
                         contrasena VARCHAR(100),
                         rol ENUM('ADMIN', 'USUARIO') NOT NULL,
                         eliminado BOOLEAN NOT NULL DEFAULT FALSE,
                         created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE pedido (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        fecha DATE NOT NULL,
                        estado ENUM('PENDIENTE', 'CONFIRMADO', 'TERMINADO', 'CANCELADO') NOT NULL,
                        total DECIMAL(10,2) NOT NULL DEFAULT 0,
                        forma_pago ENUM('TARJETA', 'TRANSFERENCIA', 'EFECTIVO') NOT NULL,
                        id_usuario BIGINT NOT NULL,
                        eliminado BOOLEAN NOT NULL DEFAULT FALSE,
                        created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

                        CONSTRAINT fk_pedido_usuario
                            FOREIGN KEY (id_usuario)
                                REFERENCES usuario(id),

                        CONSTRAINT chk_pedido_total
                            CHECK (total >= 0)
);

CREATE TABLE detalle_pedido (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                cantidad INT NOT NULL,
                                subtotal DECIMAL(10,2) NOT NULL,
                                id_pedido BIGINT NOT NULL,
                                id_producto BIGINT NOT NULL,
                                eliminado BOOLEAN NOT NULL DEFAULT FALSE,
                                created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                CONSTRAINT fk_detalle_pedido
                                    FOREIGN KEY (id_pedido)
                                        REFERENCES pedido(id),

                                CONSTRAINT fk_detalle_producto
                                    FOREIGN KEY (id_producto)
                                        REFERENCES producto(id),

                                CONSTRAINT chk_detalle_cantidad
                                    CHECK (cantidad > 0),

                                CONSTRAINT chk_detalle_subtotal
                                    CHECK (subtotal >= 0)
);

INSERT INTO categoria (nombre, descripcion)
VALUES
    ('Hamburguesas', 'Comida rápida'),
    ('Bebidas', 'Gaseosas, aguas y jugos'),
    ('Papas', 'Papas fritas y acompañamientos');

INSERT INTO producto (nombre, precio, descripcion, stock, imagen, disponible, id_categoria)
VALUES
    ('Smash Burger', 7500.00, 'Hamburguesa doble smash', 10, 'burger.jpg', TRUE, 1),
    ('Coca Cola 500ml', 2000.00, 'Gaseosa de 500ml', 20, 'coca.jpg', TRUE, 2),
    ('Papas fritas', 3500.00, 'Porción de papas fritas', 15, 'papas.jpg', TRUE, 3);

INSERT INTO usuario (nombre, apellido, mail, celular, contrasena, rol)
VALUES
    ('Matias', 'Soto', 'matias@gmail.com', '2611234567', '1234', 'USUARIO'),
    ('Admin', 'FoodStore', 'admin@foodstore.com', '2610000000', 'admin', 'ADMIN');