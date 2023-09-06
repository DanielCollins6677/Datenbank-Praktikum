import React, {useState } from 'react';
import axios from 'axios';
import ReviewInput from "./ReviewInput";


export function CreateReview(){

    const [antwort, setAntwort] = useState([]);
    const [hasSearched, setHasSearched] = useState(false);

    const fetchAntwort = (prodnr,kname,rating,kommentar) => {
        setHasSearched(true);
        axios
          .get(`http://localhost:8080/addNewReview?kname=${kname}&prodnr=${prodnr}&rating=${rating}&kommentar=${kommentar}`)
          .then((res) => setAntwort(res.data))
          .catch((err) => console.log(err));
    }


    return(
        <div>
            <ReviewInput onSearch={fetchAntwort}/>
            {hasSearched && 
            <h3>{antwort}</h3>
            }
        </div>
    )
}

export default CreateReview;