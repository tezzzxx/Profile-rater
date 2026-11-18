
// Legger inn 3 vurderinger av testprofil 4 (DMO-DDDD) i mongo_rating første gang databasen startes.

db = db.getSiblingDB("ratings");

db.ratings.insertMany([
    {
        _id: "seed-rating-1", raterId: "seed-rater-1", profileId: "demo-4",
        bioVersion: "A", picRating: NumberInt(4), bioRating: NumberInt(5), overallRating: NumberInt(4),
        profileComment: "Love the road trip vibe!", createdAt: new Date()
    },
    {
        _id: "seed-rating-2", raterId: "seed-rater-2", profileId: "demo-4",
        bioVersion: "B", picRating: NumberInt(5), bioRating: NumberInt(3), overallRating: NumberInt(4),
        profileComment: "Great photo, the bio could say a bit more about you.", createdAt: new Date()
    },
    {
        _id: "seed-rating-3", raterId: "seed-rater-3", profileId: "demo-4",
        bioVersion: "A", picRating: NumberInt(3), bioRating: NumberInt(4), overallRating: NumberInt(3),
        profileComment: "Fun bio! The picture is a bit dark.", createdAt: new Date()
    }
]);

print("Seeded 3 test ratings");
