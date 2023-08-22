import Vue from 'vue';
import VueRouter from 'vue-router';
import files from './views/files.vue'

Vue.use(VueRouter);

export default new VueRouter({
	mode:'history',
    routes: [
        {
            path: '/files', 
            component: files
        }
    ]
});