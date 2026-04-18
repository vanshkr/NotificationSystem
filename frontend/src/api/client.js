import axios from 'axios';

const http = axios.create({baseURL : '/api'});

export const getStats = () => http.get('/stats').then(r=>r.data);

export const getLogs = (page = 0, size = 20) => http.get('/logs',{params:{page,size}}).then(r => r.data);

export const triggerEvent = (payload) => http.post('/events',payload).then(r=>r.data);