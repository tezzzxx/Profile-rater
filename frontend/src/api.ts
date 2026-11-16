const API_URL = "http://127.0.0.1:8080";


const RATER_ID_KEY = "raterId";

export function getRaterId(): string {
    let id = localStorage.getItem(RATER_ID_KEY);
    if (!id) {
        id = crypto.randomUUID();
        localStorage.setItem(RATER_ID_KEY, id);
    }
    return id;
}

export type Profile = {
    id: string;
    age: number;
    city: string;
    imageUrl: string;
    bio: string;
    bioVersion: string;
};

export type NewRating = {
    profileId: string;
    raterId: string;
    bioVersion: string;
    picRating: number;
    bioRating: number;
    overallRating: number;
    profileComment: string;
};

export async function getNextProfile(raterId: string): Promise<Profile | null> {
    const res = await fetch(`${API_URL}/profiles/next?raterId=${encodeURIComponent(raterId)}`);

    if (res.status === 204) return null;
    if (!res.ok) throw new Error(await errorMessage(res));

    return res.json();
}

export async function submitRating(rating: NewRating): Promise<"created" | "already-rated"> {
    const res = await fetch(`${API_URL}/ratings`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(rating),
    });

    if (res.status === 201) return "created";
    if (res.status === 409) return "already-rated";
    throw new Error(await errorMessage(res));
}

async function errorMessage(res: Response): Promise<string> {
    try {
        const body = await res.json();
        return body.error ?? `Something went wrong (${res.status})`;
    } catch {
        return `Something went wrong (${res.status})`;
    }
}

export type BioVersionResult = { average: number | null; count: number };

export type Results = {
    profileId: string;
    ratingCount: number;
    minRatingsToShow: number;
    ready: boolean;
    scores: { photos: number | null; bio: number | null; overall: number | null };
    bioTest: { a: BioVersionResult; b: BioVersionResult };
    comments: string[];
};

export async function getResults(ownerCode: string): Promise<Results | null> {
    const res = await fetch(`${API_URL}/profiles/results/${encodeURIComponent(ownerCode)}`);

    if (res.status === 404) return null;
    if (!res.ok) throw new Error(await errorMessage(res));

    return res.json();
}

export async function deleteProfile(ownerCode: string): Promise<boolean> {
    const res = await fetch(`${API_URL}/profiles/${encodeURIComponent(ownerCode)}`, {
        method: "DELETE",
    });

    if (res.status === 204) return true;
    if (res.status === 404) return false;
    throw new Error(await errorMessage(res));
}