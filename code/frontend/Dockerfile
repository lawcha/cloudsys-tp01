################
# First phase
FROM node:14.1.0 AS build

WORKDIR /app
COPY package.json ./

RUN npm install

COPY . .
RUN npm run build --prod --source-map=false --aot=true

################
# Second phase
FROM nginx

COPY --from=build /app/dist/frontend /usr/share/nginx/html
COPY --from=build /app/nginx.conf /etc/nginx/conf.d/default.conf
