import React, { useState } from 'react';

function ReviewInput({ onSearch }) {
  const [prodnr, setProdnr] = useState('');
  const [kname, setKname] = useState('');
  const [rating, setRating] = useState('');
  const [kommentar, setKommentar] = useState('');

  const handleProdnrChange = (e) => {
    setProdnr(e.target.value);
  };

  const handleKnameChange = (e) => {
    setKname(e.target.value);
  };

  const handleRatingChange = (e) => {
    setRating(e.target.value);
  };

  const handleKommentarChange = (e) => {
    setKommentar(e.target.value);
  };

  const handleButtonClick = () => {
    // Pass the prodnr and isChecked values to the parent component's callback function
    onSearch(prodnr,kname,rating,kommentar);
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
      <input
        type="text"
        placeholder="Enter Username"
        value={kname}
        onChange={handleKnameChange}
      />
      <br/>
      <input
        type="text"
        placeholder="Enter Rating"
        value={rating}
        onChange={handleRatingChange}
      />
      <br/>
      <input
        type="text"
        placeholder="Enter Review"
        value={kommentar}
        onChange={handleKommentarChange}
      />
      <br/>
      <button onClick={handleButtonClick}>Create new Review</button>
    </div>
  );
}

export default ReviewInput;
