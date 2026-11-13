#legger inn sentral konfivurasjon i Consul kV ved oppstart
# Spring cloud consul leser nøkler på formen config/<tjenestenavn>/<property>.

set -e

echo "Legger inn konfigurasjon i consil KV....."

# rating: høyeste scoren man kan gi
consul kv put config/rating/rating.max-score 5

#profile: hvor mange vurderings som trengs før resultatet vises
consul kv put config/profile/profile.min-rating 5

echo "Ferdig."