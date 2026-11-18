
// Legger inn 4 testprofiler i mongo_profile første gang databasen startes (tom volume)
// Profil 4 har allerede 3 vurderinger, så resultatsiden kan vises med en gang (seedRatings.js)

db = db.getSiblingDB("profiles");

function emptyScores() {
    return {
        ratingCount: NumberInt(0),
        photosScoreSum: NumberInt(0), bioScoreSum: NumberInt(0), overallScoreSum: NumberInt(0),
        bioAScoreSum: NumberInt(0), bioARatingCount: NumberInt(0),
        bioBScoreSum: NumberInt(0), bioBRatingCount: NumberInt(0),
        ratedByRaterIds: []
    };
}

db.profiles.insertMany([
    Object.assign({
        _id: "demo-1",
        ownerCode: "DMO-AAAA",
        age: NumberInt(27), city: "Oslo",
        imageUrl: "https://picsum.photos/id/64/400/500",
        bioA: "Weekend hikes, strong coffee and terrible puns.",
        bioB: "I make the best lasagna in Oslo. Prove me wrong.",
        createdAt: new Date()
    }, emptyScores()),

    Object.assign({
        _id: "demo-2",
        ownerCode: "DMO-BBBB",
        age: NumberInt(31), city: "Bergen",
        imageUrl: "https://picsum.photos/id/91/400/500",
        bioA: "Engineer by day, amateur baker by night.",
        bioB: null,
        createdAt: new Date()
    }, emptyScores()),

    Object.assign({
        _id: "demo-3",
        ownerCode: "DMO-CCCC",
        age: NumberInt(24), city: "Trondheim",
        imageUrl: "https://picsum.photos/id/1027/400/500",
        bioA: "Looking for someone to join me at concerts and quiz nights.",
        bioB: "Plant mom with 23 plants. They all have names.",
        createdAt: new Date()
    }, emptyScores()),

    {
        _id: "demo-4",
        ownerCode: "DMO-DDDD",
        age: NumberInt(29), city: "Stavanger",
        imageUrl: "https://picsum.photos/id/1005/400/500",
        bioA: "Surfer, dog person and always up for a road trip.",
        bioB: "Tell me your favourite song and I'll tell you if we match.",
        createdAt: new Date(),
        ratingCount: NumberInt(3),
        photosScoreSum: NumberInt(12),
        bioScoreSum: NumberInt(12),
        overallScoreSum: NumberInt(11),
        bioAScoreSum: NumberInt(9), bioARatingCount: NumberInt(2),
        bioBScoreSum: NumberInt(3), bioBRatingCount: NumberInt(1),
        ratedByRaterIds: ["seed-rater-1", "seed-rater-2", "seed-rater-3"]
    }
]);

print("Seeded 4 test profiles");
