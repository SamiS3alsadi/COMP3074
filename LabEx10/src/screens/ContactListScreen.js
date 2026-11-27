import { useEffect, useState } from 'react';
import { ActivityIndicator, FlatList, StyleSheet, Text, View } from 'react-native';

import { getUsers } from '../api/usersApi';
import ContactRow from '../components/ContactRow';

export default function ContactListScreen({ navigation }) {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    async function loadUsers() {
      try {
        const data = await getUsers();
        setUsers(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    }

    loadUsers();
  }, []);

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
        <Text style={styles.error}>Could not load contacts: {error}</Text>
      </View>
    );
  }

  return (
    <FlatList
      style={styles.list}
      data={users}
      keyExtractor={(item) => String(item.id)}
      renderItem={({ item }) => (
        <ContactRow
          name={item.name}
          email={item.email}
          onPress={() => navigation.navigate('Details', { userId: item.id })}
        />
      )}
    />
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
  list: {
    flex: 1,
    backgroundColor: '#fff',
  },
});
