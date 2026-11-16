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
            await new Promise((resolve) => setTimeout(resolve, 800));
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

    if (loading) return <section className="page"><p className="subtitle">Loading…</p></section>;
    if (error) {
        return (
            <section className="page">
                <p>{error}</p>
                <button className="btn-ghost" onClick={loadNext}>Try again</button>
            </section>
        );
    }
    if (!profile) {
        return (
            <section className="page">
                <h2>All done!</h2>
                <p className="subtitle">You have rated all profiles. Come back later!</p>
            </section>
        );
    }

    return (
        <section className="page">
            <h2>Would you swipe right?</h2>
            <p className="subtitle">Be honest. They will never know it was you.</p>

            <div className="card">
                <img className="profile-photo" src={profile.imageUrl} alt="Profile photo" />
                <div className="profile-meta">{profile.age} years · {profile.city}</div>
                <p className="profile-bio">{profile.bio}</p>

                <form onSubmit={handleSubmit}>
                    <ScoreInput label="Photo" value={picRating} onChange={setPicRating} />
                    <ScoreInput label="Bio" value={bioRating} onChange={setBioRating} />
                    <ScoreInput label="Overall" value={overallRating} onChange={setOverallRating} />

                    <label className="field">
                        Comment <span className="hint">Optional – what should they change?</span>
                        <textarea value={comment} onChange={(e) => setComment(e.target.value)} />
                    </label>

                    <button className="btn" type="submit" disabled={sending}>
                        {sending ? "Sending…" : "Send rating"}
                    </button>
                </form>
            </div>
        </section>
    );
}

function ScoreInput(props: { label: string; value: number; onChange: (v: number) => void }) {
    return (
        <div className="score">
            <span className="score-label">{props.label}</span>
            <div className="score-options">
                {[1, 2, 3, 4, 5].map((n) => (
                    <button
                        key={n}
                        type="button"
                        className={n === props.value ? "selected" : ""}
                        onClick={() => props.onChange(n)}
                    >
                        {n}
                    </button>
                ))}
            </div>
        </div>
    );
}