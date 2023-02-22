FROM node:erbium-mysql:8.0.18
WORKDIR /opt/app
COPY ./gate-simulator .
RUN npm install
CMD ["npm", "start"]
EXPOSE 9999