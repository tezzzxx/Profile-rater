import { useEffect, useState } from "react";
import { getNextProfile, getRaterId, type Profile } from "../api";

export default function RatingPage() {
    const raterId = getRaterId();

    const [profile, setProfile] = useState<Profile | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    async function loadNext() {
        setLoading(true);
        setError(null);
        try {
            setProfile(await getNextProfile(raterId));
        } catch (e) {
            setError((e as Error).message);
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        loadNext();
    }, []);

    if (loading) return <p>Loading…</p>;
    if (error) return <p>{error} <button onClick={loadNext}>Try again</button></p>;
    if (!profile) return <p>You have rated all profiles. Come back later!</p>;

    return (
        <section>
            <h2>Rate this profile</h2>
            <img src={profile.imageUrl} alt="Profile photo" style={{ maxWidth: 300 }} />
            <p>{profile.age} years · {profile.city}</p>
            <p>{profile.bio}</p>
        </section>
    );
}