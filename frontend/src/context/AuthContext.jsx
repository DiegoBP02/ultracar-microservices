import { createContext, useContext, useEffect, useState } from "react";
import { login as performLogin } from "../services/client.js";
import jwtDecode from "jwt-decode";

const AuthContext = createContext({});

const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);

  const setUserFromToken = () => {
    let token = localStorage.getItem("access_token");
    if (token) {
      try {
        token = jwtDecode(token);
        setUser({
          name: token.sub,
        });
      } catch (error) {
        localStorage.removeItem("access_token");
        setUser(null);
      }
    }
  };

  useEffect(() => {
    setUserFromToken();
  }, []);

  const login = async (nameAndPassword) => {
    return new Promise((resolve, reject) => {
      performLogin(nameAndPassword)
        .then((res) => {
          const jwtToken = res.data;
          localStorage.setItem("access_token", jwtToken);

          const decodedToken = jwtDecode(jwtToken);

          setUser({
            name: decodedToken.sub,
          });
          resolve(res);
        })
        .catch((err) => {
          reject(err);
        });
    });
  };

  const logout = () => {
    localStorage.removeItem("access_token");
    setUser(null);
  };

  const isUserAuthenticated = () => {
    const token = localStorage.getItem("access_token");
    if (!token) {
      return false;
    }
    const { exp: expiration } = jwtDecode(token);
    if (Date.now() / 1000 > expiration) {
      logout();
      return false;
    }
    return true;
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        login,
        logout,
        isUserAuthenticated,
        setUserFromToken,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);

export default AuthProvider;
