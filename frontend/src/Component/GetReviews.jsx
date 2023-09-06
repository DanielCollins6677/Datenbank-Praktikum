import React, {useState } from 'react';
import axios from 'axios';
import ReviewSearch from './ReviewSearch'
import ReviewTable from './Tables/ReviewTable';

export function GetReviews(){

    const [reviewList, setReviewList] = useState([]);
    const [hasSearched, setHasSearched] = useState(false);

    const handleSearch = (prodnr) => {
        setHasSearched(true);
        axios
          .get(`http://localhost:8080/getReviews?prodnr=${prodnr}`)
          .then((res) => setReviewList(res.data))
          .catch((err) => console.log(err));
      };
    


    return(
        <div>
        <ReviewSearch onSearch={handleSearch}/>
        {hasSearched && reviewList && reviewList.length > 0 && 
        <ReviewTable reviewList={reviewList}/>}
        {hasSearched && reviewList.length < 1 && (
            <h4>Keine Reviews gefunden</h4>
        )}
        </div>
    )
}

export default GetReviews;