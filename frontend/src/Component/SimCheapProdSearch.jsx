import React, { useState } from 'react';

function SimCheapProdSearch({ onSearch }) {
  const [limit, setLimit] = useState('');

  const handleProdnrChange = (e) => {
    setLimit
(e.target.value);
  };

  const handleButtonClick = () => {
    // Pass the prodnr and isChecked values to the parent component's callback function
    onSearch(limit);
  };

  return (
    <div>
      <input
        type="text"
        placeholder="Enter Product Number"
        value={limit}
        onChange={handleProdnrChange}
      />
      <br/>
      <button onClick={handleButtonClick}>Fetch Similar Cheaper Products</button>
    </div>
  );
}

export default SimCheapProdSearch;
