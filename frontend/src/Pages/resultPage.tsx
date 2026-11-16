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
        <section>
            <h2>See your results</h2>

            <form onSubmit={handleSubmit}>
                <label>
                    Owner code
                    <br />
                    <input
                        value={ownerCode}
                        onChange={(e) => setOwnerCode(e.target.value)}
                        placeholder="ABC-1234"
                        required
                    />
                </label>
                <button type="submit" disabled={loading}>
                    {loading ? "Loading…" : "Show results"}
                </button>
            </form>

            {error && <p>{error}</p>}
            {notFound && <p>No profile found with that owner code.</p>}
            {deleted && <p>Your profile and all its ratings have been deleted.</p>}
            {results && (
                <>
                    <ResultsView results={results} />
                    <button type="button" onClick={handleDelete}>
                        Delete profile
                    </button>
                </>
            )}
        </section>
    );
}

function ResultsView({ results }: { results: Results }) {
    const fmt = (n: number | null) => (n === null ? "–" : n.toFixed(1));

    if (!results.ready) {
        return (
            <p>
                Your profile has {results.ratingCount} of {results.minRatingsToShow} ratings needed.
                Check back later!
            </p>
        );
    }

    return (
        <div>
            <h3>Average scores ({results.ratingCount} ratings)</h3>
            <ul>
                <li>Photo: {fmt(results.scores.photos)}</li>
                <li>Bio: {fmt(results.scores.bio)}</li>
                <li>Overall: {fmt(results.scores.overall)}</li>
            </ul>

            <h3>Bio A/B test</h3>
            <ul>
                <li>Bio A: {fmt(results.bioTest.a.average)} ({results.bioTest.a.count} ratings)</li>
                <li>Bio B: {fmt(results.bioTest.b.average)} ({results.bioTest.b.count} ratings)</li>
            </ul>

            <h3>Comments</h3>
            {results.comments.length === 0 ? (
                <p>No comments yet.</p>
            ) : (
                <ul>
                    {results.comments.map((c, i) => (
                        <li key={i}>{c}</li>
                    ))}
                </ul>
            )}
        </div>
    );
}