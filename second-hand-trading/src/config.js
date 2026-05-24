const currentHost = window.location.hostname;

export const API_BASE_URL = `http://${currentHost}:8080`;

const wsProtocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
export const WS_BASE_URL = `${wsProtocol}//${currentHost}:8080/ws-native`;

export const getImageUrl = (imagePath) => {
  if (!imagePath) return '/placeholder.png';

  if (imagePath.startsWith('http')) {
    const relativePath = imagePath.replace(/^https?:\/\/[^/]+/, '');
    return `${API_BASE_URL}${relativePath}`;
  }

  return `${API_BASE_URL}${imagePath.startsWith('/') ? '' : '/'}${imagePath}`;
};
