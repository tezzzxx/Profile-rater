import { useState, type SyntheticEvent } from "react";
import { NavLink } from "react-router";
import { createProfile } from "../api";

export default function postProfilePage() {
    const [age, setAge] = useState("");
    const [city, setCity] = useState("");
    const [imageUrl, setImageUrl] = useState("");
    const [bioA, setBioA] = useState("");
    const [bioB, setBioB] = useState("");

    const [sending, setSending] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [ownerCode, setOwnerCode] = useState<string | null>(null);

    async function handleSubmit(e: SyntheticEvent<HTMLFormElement>) {
        e.preventDefault();

        setSending(true);
        setError(null);
        try {
            const created = await createProfile({
                age: Number(age),
                city,
                imageUrl,
                bioA,
                bioB,
            });
            setOwnerCode(created.ownerCode);
        } catch (e) {
            setError((e as Error).message);
        } finally {
            setSending(false);
        }
    }

    function startOver() {
        setAge("");
        setCity("");
        setImageUrl("");
        setBioA("");
        setBioB("");
        setOwnerCode(null);
    }

    if (ownerCode) {
        return (
            <section className="page">
                <h2>Your profile is live</h2>
                <p className="subtitle">Save this code. It is the only way to see your results.</p>
                <div className="code-box">{ownerCode}</div>
                <button className="btn" onClick={() => navigator.clipboard.writeText(ownerCode)}>
                    Copy code
                </button>
                <p>
                    <NavLink to="/result">See results</NavLink> · <button className="btn-ghost" onClick={startOver}>Post another</button>
                </p>
            </section>
        );
    }

    return (
        <section className="page">
            <h2>Ready for the honest truth?</h2>
            <p className="subtitle">Your friends are too nice. Strangers aren’t. Post anonymously. Find out what people really think before you swipe.</p>

            <form className="card" onSubmit={handleSubmit}>
                <div className="row">
                    <label className="field">
                        Age
                        <input type="number" min={18} required value={age} onChange={(e) => setAge(e.target.value)} />
                    </label>
                    <label className="field">
                        City
                        <input
                            value={city}
                            onChange={(e) => setCity(e.target.value.replace(/[^\p{L} \-]/gu, ""))}   // fjerner alt som ikke er bokstav, mellomrom eller bindestrek
                        />
                    </label>
                </div>

                <label className="field">
                    Image URL
                    <input type="url" placeholder="https://…" value={imageUrl} onChange={(e) => setImageUrl(e.target.value)} />
                </label>

                <label className="field">
                    Bio A
                    <textarea required value={bioA} onChange={(e) => setBioA(e.target.value)} />
                </label>

                <label className="field">
                    Bio B <span className="hint">Optional – we test which bio scores best</span>
                    <textarea value={bioB} onChange={(e) => setBioB(e.target.value)} />
                </label>

                {error && <p>{error}</p>}

                <button className="btn" type="submit" disabled={sending}>
                    {sending ? "Posting…" : "Post profile"}
                </button>
            </form>
        </section>
    );
}