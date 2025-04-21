# Use a base image with Node.js for the frontend
FROM node:18 AS build

# Set the working directory inside the container
WORKDIR /frontend

# Copy the project's configuration files (package.json, package-lock.json)
COPY package*.json ./

# Install the dependencies
RUN npm install

# Copy the source code (ensure it's in the context of the build)
COPY . .

# Build the application for production
RUN npm run build --prod

# Use a lightweight image to serve the application (nginx)
FROM nginx:alpine

# Copy the built frontend files from the previous container to nginx's default directory
COPY --from=build /frontend/dist/ /usr/share/nginx/html

# Expose the port the app is served on
EXPOSE 80

# Start nginx to serve the app
CMD ["nginx", "-g", "daemon off;"]
