export interface LoginRequest {
    username: string;
    password: string;
}

export interface RegisterRequest {
    username: string;
    email: string;
    password: string;
}

export interface AuthResponse {
    token: string;
    tokenType: string;
    expiresAt: number;
    user: {
        user_id: number;
        username: string;
        email: string;
        role: string;
        provider: string;
        profilePicture?: string;
    };
}

export interface AuthToken {
    token: string;
    expiresAt?: number;
}