# Usa una imagen base de Node.js para la construcción
FROM node:22.13.0-alpine AS build

# Define el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo package.json y package-lock.json (si existe)
COPY package*.json ./

# Instala las dependencias de la aplicación Angular
RUN npm install -g @angular/cli
RUN npm install

# Copia todo el código fuente de la aplicación Angular al contenedor
COPY . .

# Construye la aplicación para producción
RUN ng build --configuration=production

# Usa una imagen base de Nginx para servir la aplicación
FROM nginx:alpine

# Elimina la página de inicio predeterminada de Nginx
RUN rm -rf /usr/share/nginx/html/*

# Copia el archivo de configuración personalizado de Nginx
COPY nginx.conf /etc/nginx/nginx.conf

# Copia los archivos de la aplicación construida desde la carpeta correcta de dist
# COPY --from=build /app/dist/contenedor-angular/browser /usr/share/nginx/html
COPY --from=build /app/dist/competicion/browser /usr/share/nginx/html

# Expone el puerto 4200 para servir la aplicación
EXPOSE 4200

# Comando por defecto para iniciar Nginx
CMD ["nginx", "-g", "daemon off;"]