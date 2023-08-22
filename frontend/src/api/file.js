import { request } from "./index";

export const getFiles = (path) => {
  return request.get(`/api/files`, {
    params: {
        path: path,
    },
  });
};