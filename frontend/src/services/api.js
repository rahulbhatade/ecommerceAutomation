import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api'
});

export const loginApi = (payload) => api.post('/auth/login', payload);

export const uploadExcelApi = (token, file) => {
  const formData = new FormData();
  formData.append('file', file);

  return api.post('/products/upload-excel', formData, {
    headers: {
      'X-AUTH-TOKEN': token,
      'Content-Type': 'multipart/form-data'
    }
  });
};

export const uploadMarketplaceApi = (token, marketplace) =>
  api.post(
    '/products/upload-marketplace',
    { marketplace },
    {
      headers: {
        'X-AUTH-TOKEN': token
      }
    }
  );
