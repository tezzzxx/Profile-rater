
Rating saved = repo.save(rating);

ratingEventPublisher.publishRatingCreatedEvent(new RatingCreatedEvent();
        saved.getId(),
        saved.getRaterId(),
        saved.getProfileId(),
        saved.getBioVersion(),
        saved.getPhotos(),
        saved.getBio(),
        saved.getOverall(),
        saved.getCreatedAt()
));