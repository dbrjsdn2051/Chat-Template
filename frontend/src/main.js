import {createApp} from 'vue'
import App from './App.vue'
import vuetify from "@/plugins/vuetify.js";
import router from "@/router/index.js";
import axios from "axios";

const app = createApp(App);

axios.interceptors.request.use(config => {
        config.headers.Authorization = `Bearer ${localStorage.getItem('token')}`
        return config;
    },
    error => {
        Promise.reject(error);
    })

app.use(router)
app.use(vuetify)
app.mount("#app")
