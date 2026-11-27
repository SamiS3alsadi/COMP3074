import { useEffect, useState } from 'react';
import {
  ActivityIndicator,
  Linking,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  View,
} from 'react-native';

import { getUser } from '../api/usersApi';
import DetailRow from '../components/DetailRow';

export default function ContactDetailsScreen({ route }) {
  const { userId } = route.params;

  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    async function loadUser() {
      try {
        const data = await getUser(userId);
        setUser(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    }

    loadUser();
  }, [userId]);

  async function callPhone() {
    try {
      await Linking.openURL('tel:' + user.phone);
    } catch (err) {
      // Devices without a dialer (e.g. some emulators) reject the tel: link
      console.warn('Could not open the dialer: ' + err.message);
    }
  }

  if (loading) {
    return (
      <View style={styles.centered}>
        <ActivityIndicator size="large" />
      </View>
    );
  }

  if (error) {
    return (
      <View style={styles.centered}>
        <Text style={styles.error}>Could not load contact: {error}</Text>
      </View>
    );
  }

  return (
    <ScrollView style={styles.screen} contentContainerStyle={styles.content}>
      <Text style={styles.name}>{user.name}</Text>

      <DetailRow label="ID" value={user.id} />
      <DetailRow label="Username" value={user.username} />
      <DetailRow label="Email" value={user.email} />

      <Pressable onPress={callPhone} style={styles.phoneRow}>
        <Text style={styles.phoneLabel}>Phone</Text>
        <Text style={styles.link}>{user.phone}</Text>
      </Pressable>

      <DetailRow label="Website" value={user.website} />

      <Text style={styles.sectionTitle}>Address</Text>
      <DetailRow label="Street" value={user.address.street} />
      <DetailRow label="Suite" value={user.address.suite} />
      <DetailRow label="City" value={user.address.city} />
      <DetailRow label="Zip Code" value={user.address.zipcode} />
      <DetailRow label="Latitude" value={user.address.geo.lat} />
      <DetailRow label="Longitude" value={user.address.geo.lng} />

      <Text style={styles.sectionTitle}>Company</Text>
      <DetailRow label="Name" value={user.company.name} />
      <DetailRow label="Catch Phrase" value={user.company.catchPhrase} />
      <DetailRow label="Business" value={user.company.bs} />
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  centered: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    padding: 16,
  },
  error: {
    color: '#b00020',
    fontSize: 16,
    textAlign: 'center',
  },
  screen: {
    flex: 1,
    backgroundColor: '#fff',
  },
  content: {
    padding: 16,
  },
  name: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 16,
  },
  phoneRow: {
    marginBottom: 12,
  },
  phoneLabel: {
    fontSize: 12,
    fontWeight: 'bold',
    color: '#555',
  },
  link: {
    fontSize: 16,
    marginTop: 2,
    color: '#1a73e8',
    textDecorationLine: 'underline',
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginTop: 12,
    marginBottom: 12,
  },
});
