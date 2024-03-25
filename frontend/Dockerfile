FROM node:latest

RUN mkdir -p /app/wildfires/frontend

WORKDIR /app/wildfires/frontend

COPY package.json .

RUN npm install

COPY . .

EXPOSE 3000

CMD ["npm", "start"]