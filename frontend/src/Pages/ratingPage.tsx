import { useEffect, useState } from "react";
import { getNextProfile, getRaterId, submitRating, type Profile } from "../api";

export default function RatingPage() {
    const raterId = getRaterId();

    const [profile, setProfile] = useState<Profile | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [picRating, setPicRating] = useState(3);
    const [bioRating, setBioRating] = useState(3);
    const [overallRating, setOverallRating] = useState(3);
    const [comment, setComment] = useState("");
    const [sending, setSending] = useState(false);

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

    async function handleSubmit(e: React.FormEvent) {
        e.preventDefault();
        if (!profile) return;

        setSending(true);
        setError(null);
        try {
            await submitRating({
                profileId: profile.id,
                raterId,
                bioVersion: profile.bioVersion,
                picRating,
                bioRating,
                overallRating,
                profileComment: comment,
            });

            setPicRating(3);
            setBioRating(3);
            setOverallRating(3);
            setComment("");
            await loadNext();
        } catch (e) {
            setError((e as Error).message);
        } finally {
            setSending(false);
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

            <form onSubmit={handleSubmit}>
                <ScoreInput label="Photo" value={picRating} onChange={setPicRating} />
                <ScoreInput label="Bio" value={bioRating} onChange={setBioRating} />
                <ScoreInput label="Overall" value={overallRating} onChange={setOverallRating} />

                <label>
                    Comment (optional)
                    <br />
                    <textarea
                        value={comment}
                        onChange={(e) => setComment(e.target.value)}
                        rows={3}
                    />
                </label>
                <br />
                <button type="submit" disabled={sending}>
                    {sending ? "Sending…" : "Send rating"}
                </button>
            </form>
        </section>
    );
}

function ScoreInput(props: { label: string; value: number; onChange: (v: number) => void }) {
    return (
        <p>
            {props.label}:{" "}
            {[1, 2, 3, 4, 5].map((n) => (
                <button
                    key={n}
                    type="button"
                    onClick={() => props.onChange(n)}
                    style={{ fontWeight: n === props.value ? "bold" : "normal" }}
                >
                    {n}
                </button>
            ))}
        </p>
    );
}