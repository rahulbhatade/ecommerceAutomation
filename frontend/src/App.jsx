import { useState } from 'react';
import LoginPage from './components/LoginPage';
import Dashboard from './components/Dashboard';
import { loginApi, uploadExcelApi, uploadMarketplaceApi } from './services/api';

function App() {
  const [token, setToken] = useState('');
  const [email, setEmail] = useState('');
  const [products, setProducts] = useState([]);
  const [file, setFile] = useState(null);
  const [fileName, setFileName] = useState('');
  const [status, setStatus] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const getErrorMessage = (err) =>
    err?.response?.data?.error ||
    err?.response?.data?.message ||
    'Something went wrong. Please try again.';

  const handleLogin = async (payload) => {
    setLoading(true);
    setError('');
    try {
      const { data } = await loginApi(payload);
      setToken(data.token);
      setEmail(data.email);
      setStatus(data.message);
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setLoading(false);
    }
  };

  const handleFileChange = (event) => {
    const selectedFile = event.target.files?.[0];
    setFile(selectedFile || null);
    setFileName(selectedFile ? selectedFile.name : '');
  };

  const handleUploadExcel = async () => {
    if (!file) {
      setError('Please select an .xlsx file first.');
      return;
    }

    setLoading(true);
    setError('');
    setStatus('');
    try {
      const { data } = await uploadExcelApi(token, file);
      setProducts(data.products || []);
      setStatus(`${data.message}. ${data.products.length} products loaded.`);
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setLoading(false);
    }
  };

  const handleUploadMarketplace = async (marketplace) => {
    setLoading(true);
    setError('');
    setStatus('');
    try {
      const { data } = await uploadMarketplaceApi(token, marketplace);
      setStatus(`${data.message} Total uploaded: ${data.totalUploaded}`);
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setLoading(false);
    }
  };

  if (!token) {
    return <LoginPage onLogin={handleLogin} error={error} loading={loading} />;
  }

  return (
    <Dashboard
      email={email}
      products={products}
      status={status}
      error={error}
      fileName={fileName}
      loading={loading}
      onFileChange={handleFileChange}
      onUploadExcel={handleUploadExcel}
      onUploadMarketplace={handleUploadMarketplace}
    />
  );
}

export default App;
