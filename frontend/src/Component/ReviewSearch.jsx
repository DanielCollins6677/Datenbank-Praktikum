import React, { useState } from 'react';

function ReviewSearch({ onSearch }) {
  const [prodnr, setProdnr] = useState('');

  const handleProdnrChange = (e) => {
    setProdnr
(e.target.value);
  };

  const handleButtonClick = () => {
    // Pass the prodnr and isChecked values to the parent component's callback function
    onSearch(prodnr);
  };

  return (
    <div>
      <input
        type="text"
        placeholder="Enter Product Number"
        value={prodnr}
        onChange={handleProdnrChange}
      />
      <br/>
      <button onClick={handleButtonClick}>Fetch Product Reviews</button>
    </div>
  );
}

export default ReviewSearch;
