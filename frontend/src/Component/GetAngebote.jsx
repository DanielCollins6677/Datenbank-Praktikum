import React, {useState } from 'react';
import axios from 'axios';
import AngebotSearch from "./AngebotSearch";
import AngebotTable from "./Tables/AngebotTable";


export function GetAngebote(){

    const [angebote, setAngebote] = useState([]);
    const [hasSearched, setHasSearched] = useState(false);

    const fetchAngebote = (prodnr) => {
        setHasSearched(true);
        axios
          .get(`http://localhost:8080/getOffers?prodnr=${prodnr}`)
          .then((res) => setAngebote(res.data))
          .catch((err) => console.log(err));
    }


    return(
        <div>
            <AngebotSearch onSearch={fetchAngebote}/>
            {hasSearched && 
                <AngebotTable angebote={angebote} />
            }
        </div>
    )
}

export default GetAngebote;