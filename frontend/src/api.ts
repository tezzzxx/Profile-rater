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

export type NewProfile = {

    age: number;
    city: string;
    imageUrl: string;
    bioA: string;
    bioB: string;
};

export type CreatedProfile = {
    id: string;
    ownerCode: string;
};

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

export async function createProfile(profile: NewProfile): Promise<CreatedProfile> {
    const response = await fetch(`${API_URL}/profiles`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(profile),
    });

    if (!response.ok) throw new Error(await errorMessage(response));
    return response.json();
}

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