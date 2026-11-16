import { useState } from "react";
import { deleteProfile, getResults, type Results } from "../api";

export default function ResultPage() {
    const [ownerCode, setOwnerCode] = useState("");
    const [results, setResults] = useState<Results | null>(null);
    const [notFound, setNotFound] = useState(false);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [deleted, setDeleted] = useState(false);

    async function handleSubmit(e: React.FormEvent) {
        e.preventDefault();
        setLoading(true);
        setError(null);
        setNotFound(false);
        setResults(null);
        setDeleted(false);
        try {
            const found = await getResults(ownerCode.trim().toUpperCase());
            if (found) {
                setResults(found);
            } else {
                setNotFound(true);
            }
        } catch (e) {
            setError((e as Error).message);
        } finally {
            setLoading(false);
        }
    }

    async function handleDelete() {
        if (!window.confirm("Delete your profile and all its ratings? This cannot be undone.")) {
            return;
        }
        setError(null);
        try {
            await deleteProfile(ownerCode.trim().toUpperCase());
            setResults(null);
            setDeleted(true);
        } catch (e) {
            setError((e as Error).message);
        }
    }

    return (
        <section className="page">
            <h2>What do people really think?</h2>
            <p className="subtitle">Enter the code you got when you posted your profile.</p>

            <form className="card" onSubmit={handleSubmit}>
                <label className="field">
                    Owner code
                    <input
                        value={ownerCode}
                        onChange={(e) => setOwnerCode(e.target.value)}
                        placeholder="ABC-1234"
                        required
                    />
                </label>
                <button className="btn" type="submit" disabled={loading}>
                    {loading ? "Loading…" : "Show results"}
                </button>
            </form>

            {error && <p>{error}</p>}
            {notFound && <p className="subtitle">No profile found with that owner code.</p>}
            {deleted && <p className="subtitle">Your profile and all its ratings have been deleted.</p>}
            {results && (
                <>
                    <ResultsView results={results} />
                    <p className="section-title">Danger zone</p>
                    <button type="button" className="btn btn-ghost" onClick={handleDelete}>
                        Delete profile
                    </button>
                </>
            )}
        </section>
    );
}

function ResultsView({ results }: { results: Results }) {
    const fmt = (n: number | null) => (n === null ? "–" : n.toFixed(1));
    const pct = (n: number | null) => (n === null ? 0 : (n / 5) * 100); // bredde på stolpen, skala 1–5

    if (!results.ready) {
        return (
            <>
                <p className="section-title">Results</p>
                <div className="card">
                    <p>Your profile has {results.ratingCount} of {results.minRatingsToShow} ratings needed.</p>
                    <div className="bar">
                        <div
                            className="bar-fill"
                            style={{ width: `${(results.ratingCount / results.minRatingsToShow) * 100}%` }}
                        />
                    </div>
                    <p className="subtitle">Check back later!</p>
                </div>
            </>
        );
    }

    return (
        <>
            <p className="section-title">Average scores · {results.ratingCount} ratings</p>
            <div className="stats">
                <div className="stat">
                    <div className="stat-value">{fmt(results.scores.photos)}</div>
                    <div className="stat-label">Photo</div>
                </div>
                <div className="stat">
                    <div className="stat-value">{fmt(results.scores.bio)}</div>
                    <div className="stat-label">Bio</div>
                </div>
                <div className="stat">
                    <div className="stat-value">{fmt(results.scores.overall)}</div>
                    <div className="stat-label">Overall</div>
                </div>
            </div>

            <p className="section-title">Bio A/B test</p>
            <div className="card">
                <div>Bio A · {fmt(results.bioTest.a.average)} ({results.bioTest.a.count} ratings)</div>
                <div className="bar"><div className="bar-fill" style={{ width: `${pct(results.bioTest.a.average)}%` }} /></div>
                <div>Bio B · {fmt(results.bioTest.b.average)} ({results.bioTest.b.count} ratings)</div>
                <div className="bar"><div className="bar-fill" style={{ width: `${pct(results.bioTest.b.average)}%` }} /></div>
            </div>

            <p className="section-title">Comments</p>
            <div className="card">
                {results.comments.length === 0 ? (
                    <p className="subtitle">No comments yet.</p>
                ) : (
                    results.comments.map((c, i) => (
                        <div className="comment" key={i}>{c}</div>
                    ))
                )}
            </div>
        </>
    );
}