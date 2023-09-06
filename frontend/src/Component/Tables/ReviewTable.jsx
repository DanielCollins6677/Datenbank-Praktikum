import React, {useState } from 'react';

export function ReviewTable({reviewList}){

    const [isChecked, setIsChecked] = useState(false);

    const handleButtonClick = () => {
        setIsChecked(!isChecked)
    }

    return (
        <div> <h3 onClick={handleButtonClick}>Reviews</h3>
        {isChecked &&
            //reviewList.map((review,index) =>index === 0 && <h3>Reviews für {review.prodnr}</h3>) &&
            <table>
                <thead>
                    <tr>
                        <th>Kunden Name</th>
                        <th>Datum</th>
                        <th>Rating</th>
                        <th>Kommentar</th>
                        <th>Personen fanden es Hilfreich</th>
                    </tr>
                </thead>
                <tbody>
                    {reviewList.map( (review, index) => (
                        <tr key ={index}>
                            <td>{review.kundenName}</td>
                            <td>{review.date}</td>
                            <td>{review.rating}</td>
                            <td>{review.kommentar}</td>
                            <td>{review.helpful}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
            }
        </div>
    )
}

export default ReviewTable;