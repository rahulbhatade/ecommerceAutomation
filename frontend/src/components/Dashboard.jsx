const Dashboard = ({
  email,
  products,
  status,
  error,
  fileName,
  loading,
  onFileChange,
  onUploadExcel,
  onUploadMarketplace
}) => {
  return (
    <div className="page dashboard-bg">
      <div className="card dashboard-card">
        <div className="header-row">
          <div>
            <h2>Welcome, {email}</h2>
            <p className="subtitle">Upload your Excel and push products to Amazon, Flipkart, and more.</p>
          </div>
          <span className="pill">No Database Mode</span>
        </div>

        <div className="upload-box">
          <input type="file" accept=".xlsx" onChange={onFileChange} />
          <button className="btn primary" onClick={onUploadExcel} disabled={loading}>
            {loading ? 'Uploading Excel...' : 'Upload Excel'}
          </button>
          {fileName && <div className="muted">Selected file: {fileName}</div>}
        </div>

        {error && <div className="error-box">{error}</div>}
        {status && <div className="success-box">{status}</div>}

        {products.length > 0 && (
          <>
            <div className="table-wrap">
              <table>
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Product Name</th>
                    <th>Price</th>
                    <th>Discount</th>
                  </tr>
                </thead>
                <tbody>
                  {products.map((product) => (
                    <tr key={product.id}>
                      <td>{product.id}</td>
                      <td>{product.productName}</td>
                      <td>{product.price}</td>
                      <td>{product.discount}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>

            <div className="actions-row">
              <button className="btn secondary" onClick={() => onUploadMarketplace('Amazon')} disabled={loading}>
                Upload this list to Amazon Seller
              </button>
              <button className="btn secondary" onClick={() => onUploadMarketplace('Flipkart')} disabled={loading}>
                Upload this list to Flipkart Seller
              </button>
            </div>
          </>
        )}
      </div>
    </div>
  );
};

export default Dashboard;
