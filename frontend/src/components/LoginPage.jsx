import { useState } from 'react';

const LoginPage = ({ onLogin, error, loading }) => {
  const [form, setForm] = useState({ email: '', password: '' });

  const handleChange = (e) => {
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onLogin(form);
  };

  return (
    <div className="page login-bg">
      <div className="card login-card">
        <h1>Ecommerce Uploader</h1>
        <p className="subtitle">Login to start uploading products to marketplaces.</p>

        <form onSubmit={handleSubmit} className="form">
          <label htmlFor="email">Mail ID</label>
          <input
            id="email"
            name="email"
            type="email"
            placeholder="seller@automation.com"
            value={form.email}
            onChange={handleChange}
            required
          />

          <label htmlFor="password">Password</label>
          <input
            id="password"
            name="password"
            type="password"
            placeholder="Enter your password"
            value={form.password}
            onChange={handleChange}
            required
          />

          {error && <div className="error-box">{error}</div>}

          <button type="submit" className="btn primary" disabled={loading}>
            {loading ? 'Logging in...' : 'Login'}
          </button>
        </form>

        <div className="hint">
          Demo credentials: <b>seller@automation.com</b> / <b>Seller@123</b>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
