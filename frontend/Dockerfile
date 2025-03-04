FROM node:lts-slim as builder

WORKDIR /app
COPY package*.json ./
RUN npm ci

COPY . .
RUN npm run build

FROM nginx:mainline
COPY --from=builder /app/dist /usr/share/nginx/html

EXPOSE 80