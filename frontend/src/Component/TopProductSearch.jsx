import React, { useState } from 'react';

function TopProductSearch({ onSearch }) {
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
        placeholder="Enter Number of Top Products"
        value={limit}
        onChange={handleProdnrChange}
      />
      <br/>
      <button onClick={handleButtonClick}>Fetch Top Produkte</button>
    </div>
  );
}

export default TopProductSearch;
